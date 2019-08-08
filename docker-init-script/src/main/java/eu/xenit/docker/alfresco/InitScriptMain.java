package eu.xenit.docker.alfresco;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class InitScriptMain {

    public static void main(String[] argv) throws IOException {
        String globalPropertiesFile = argv[0];
        String tomcatConfigFile = argv[1];
        Properties globalProperties = new Properties();
        try (InputStream globalPropertiesInputStream = new FileInputStream(globalPropertiesFile)) {
            globalProperties.load(globalPropertiesInputStream);
        }
        InitScriptMain main = new InitScriptMain(new ThrowingMap<String, String>(System.getenv(), "ENV"),
                new ThrowingMap<String, String>((Map) globalProperties, "global-properties"));
        main.process();
        try (OutputStream globalPropertiesOutputStream = new FileOutputStream(globalPropertiesFile)) {
            globalProperties.store(globalPropertiesOutputStream, null);
        }
        try (PrintStream tomcatConfigOutputStream = new PrintStream(new FileOutputStream(tomcatConfigFile))) {
            StringBuilder javaOpts = new StringBuilder();
            for (String javaOption : main.getJavaOptions()) {
                javaOpts.append(javaOption).append(" ");
            }
            tomcatConfigOutputStream.println("JAVA_OPTS=\"" + javaOpts.toString() + "\"");
            tomcatConfigOutputStream.println("export JAVA_OPTS");
        }
    }


    private final Map<String, String> environment;
    private final Map<String, String> globalProperties;
    private final AlfrescoVersion alfrescoVersion;

    public List<String> getJavaOptions() {
        return Collections.unmodifiableList(javaOptions);
    }

    private final List<String> javaOptions = new ArrayList<>();

    public InitScriptMain(Map<String, String> environment, Map<String, String> globalProperties) {
        this.environment = environment;
        this.globalProperties = globalProperties;
        alfrescoVersion = AlfrescoVersion.parse(environment.get("ALFRESCO_VERSION"));
        if (environment.containsKey("JAVA_OPTS")) {
            javaOptions.add(environment.get("JAVA_OPTS"));
        }
    }

    private void setGlobalOptionFromEnvironment(String option, String environmentVariable, String defaultValue) {
        String value = defaultValue;
        if (environment.containsKey(environmentVariable)) {
            value = environment.get(environmentVariable);
        }
        globalProperties.put(option, value);
    }

    private void setGlobalOptionFromEnvironment(String option, String environmentVariable) {
        if (environment.containsKey(environmentVariable)) {
            globalProperties.put(option, environment.get(environmentVariable));
        }
    }

    public void process() {
        setGlobalOptionFromEnvironment("dir.root", "DIR_ROOT", "/opt/alfresco/alf_data");
        setGlobalOptionFromEnvironment("dir.keystore", "DIR_KEYSTORE", "/opt/alfresco/keystore");

        setGlobalOptionFromEnvironment("alfresco.host", "ALFRESCO_HOST", "alfresco");
        setGlobalOptionFromEnvironment("alfresco.port", "ALFRESCO_PORT", "8080");
        setGlobalOptionFromEnvironment("alfresco.protocol", "ALFRESCO_PROTOCOL", "http");
        setGlobalOptionFromEnvironment("alfresco.context", "ALFRESCO_CONTEXT", "alfresco");

        setGlobalOptionFromEnvironment("share.host", "SHARE_HOST", "share");
        setGlobalOptionFromEnvironment("share.port", "SHARE_PORT", "8080");
        setGlobalOptionFromEnvironment("share.protocol", "SHARE_PROTOCOL", "http");
        setGlobalOptionFromEnvironment("share.context", "SHARE_CONTEXT", "share");

        // Database configuration
        setGlobalOptionFromEnvironment("db.driver", "DB_DRIVER", "org.postgresql.Driver");
        setGlobalOptionFromEnvironment("db.host", "DB_HOST");
        setGlobalOptionFromEnvironment("db.port", "DB_PORT");
        setGlobalOptionFromEnvironment("db.name", "DB_NAME");
        setGlobalOptionFromEnvironment("db.username", "DB_USERNAME", "alfresco");
        setGlobalOptionFromEnvironment("db.password", "DB_PASSWORD", "admin");
        setGlobalOptionFromEnvironment("db.url", "DB_URL",
                "jdbc:postgresql://" + environment.get("DB_HOST") + ":" + environment.get("DB_PORT") + "/" + environment.get("DB_NAME"));
        setGlobalOptionFromEnvironment("db.pool.validate.query", "DB_QUERY", "select 1");

        // Solr configuration

        String solrVersion = getDefaultSolrVersion();
        if (environment.containsKey("INDEX")) {
            solrVersion = environment.get("INDEX");
        }
        globalProperties.put("index.subsystem.name", solrVersion);
        if ("solr6".equals(solrVersion)) {
            globalProperties.put("solr.backup.alfresco.remoteBackupLocation", "/opt/alfresco-search-services");
            globalProperties.put("solr.backup.archive.remoteBackupLocation", "/opt/alfresco-search-services");
        }
        setGlobalOptionFromEnvironment("solr.host", "SOLR_HOST", "solr");
        setGlobalOptionFromEnvironment("solr.port", "SOLR_PORT", "8080");
        setGlobalOptionFromEnvironment("solr.port.ssl", "SOLR_PORT_SSL", "8443");
        setGlobalOptionFromEnvironment("solr.useDynamicShardRegistration", "DYNAMIC_SHARD_REGISTRATION", "false");
        setGlobalOptionFromEnvironment("solr.secureComms", "SOLR_SSL", "https");

        if ("none".equalsIgnoreCase(environment.get("SOLR_SSL")) && alfrescoVersion.isGreaterThan("5.0")) {
            // TODO: remove SSL connector from tomcat server.xml
        }

        // System
        setGlobalOptionFromEnvironment("mail.host", "MAIL_HOST", "localhost");
        setGlobalOptionFromEnvironment("cifs.enabled", "ENABLE_CIFS", "false");
        setGlobalOptionFromEnvironment("ftp.enabled", "ENABLE_FTP", "false");
        setGlobalOptionFromEnvironment("replication.enabled", "ENABLE_REPLICATION", "false");
        setGlobalOptionFromEnvironment("alfresco.cluster.enabled", "ENABLE_CLUSTERING", "false");

        // community versions works with ooo and Alfresco recommends to have only one system enabled at a time
        // https://docs.alfresco.com/4.2/concepts/OOo-subsystems-intro.html
        if (alfrescoVersion.isLessThan("6.0") && "community".equals(environment.get("ALFRESCO_FLAVOR"))) {
            globalProperties.put("ooo.enabled", "true");
            globalProperties.put("jodconverter.enabled", "false");
        } else {
            globalProperties.put("ooo.enabled", "false");
            globalProperties.put("jodconverter.enabled", "true");
        }

        if (alfrescoVersion.isLessThan("6.0")) {
            globalProperties.put("ooo.exe", "/usr/lib/libreoffice/program/soffice");
            globalProperties.put("jodconverter.officeHome", "/usr/lib/libreoffice/");
            globalProperties.put("img.exe", "/usr/bin/convert");
            globalProperties.put("img.root", "/etc/ImageMagick");
            globalProperties.put("img.dyn", "/usr/lib");
            globalProperties.put("swf.exe", "/usr/bin/pdf2swf");
            globalProperties.put("alfresco-pdf-renderer.root", "/opt/alfresco");
            globalProperties.put("alfresco-pdf-renderer.exe", "${alfresco-pdf-renderer.root}/alfresco-pdf-renderer");
        }

        javaOptions.add("-Xms" + environment.get("JAVA_XMS"));
        javaOptions.add("-Xmx" + environment.get("JAVA_XMX"));
        javaOptions.add("-Dfile.encoding=UTF-8");

        if ("true".equalsIgnoreCase(environment.get("JMX_ENABLED"))) {
            javaOptions.add("-Dcom.sun.management.jmxremote.authenticate=false");
            javaOptions.add("-Dcom.sun.management.jmxremote.local.only=false");
            javaOptions.add("-Dcom.sun.management.jmxremote.ssl=false");
            javaOptions.add("-Dcom.sun.management.jmxremote");
            javaOptions.add("-Dcom.sun.management.jmxremote.rmi.port=5000");
            javaOptions.add("-Dcom.sun.management.jmxremote.port=5000");
            javaOptions.add("-Djava.rmi.server.hostname=" + environment.get("JMX_RMI_HOST"));
            globalProperties.put("alfresco.jmx.connector.enabled", "true");
        }

        if ("true".equalsIgnoreCase(environment.get("DEBUG"))) {
            javaOptions.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8000");
        }

        // Special handling for alfresco 4 - set MaxPermSize
        if (alfrescoVersion.major == 4) {
            javaOptions.add("-XX:MaxPermSize=256m");
        }

        if (alfrescoVersion.isGreaterThanOrEqual("6.1")) {
            globalProperties.put("messaging.subsystem.autoStart", "false");
        }

        for (Map.Entry<String, String> entry : environment.entrySet()) {
            if (entry.getKey().startsWith("GLOBAL_")) {
                String globalProperty = entry.getKey().substring("GLOBAL_".length());
                globalProperties.put(globalProperty, entry.getValue());
            }

        }
    }

    /**
     * Derives the default solr version from the alfresco version
     */
    private String getDefaultSolrVersion() {
        if (alfrescoVersion.major == 4) {
            return "solr";
        }
        if (alfrescoVersion.isBetweenInclusive("5.0", "5.1")) {
            return "solr4";
        }

        return "solr6";
    }

}
