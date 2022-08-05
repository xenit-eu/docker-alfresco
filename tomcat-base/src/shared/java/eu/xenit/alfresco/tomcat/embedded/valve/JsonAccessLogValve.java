package eu.xenit.alfresco.tomcat.embedded.valve;

import com.google.gson.JsonObject;
import org.apache.catalina.AccessLog;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonAccessLogValve extends ValveBase implements AccessLog {

    private boolean requestAttributesEnabled = false;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    @Override
    public void log(Request request, Response response, long duration) {
        JsonObject root = new JsonObject();
        Date date = new Date();
        root.addProperty("Time", simpleDateFormat.format(date));
        root.addProperty("type", "accessLog");
        root.addProperty("duration", duration);
        root.addProperty("responseCode", response.getCoyoteResponse().getStatus());
        root.addProperty("remoteHost", request.getRemoteHost());
        root.addProperty("method", request.getMethod());
        root.addProperty("uri", request.getRequestURI());
        root.addProperty("query", request.getQueryString());
        root.addProperty("requestSize", request.getContentLength());
        root.addProperty("responseSize", response.getContentLength());
        System.out.println(root);
    }

    @Override
    public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {
        this.requestAttributesEnabled = requestAttributesEnabled;
    }

    @Override
    public boolean getRequestAttributesEnabled() {
        return requestAttributesEnabled;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        getNext().invoke(request, response);
    }
}
