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
import java.util.stream.Collectors;

public class InitScriptMain {

    public static void main(String[] argv) throws IOException {
        String globalPropertiesFile = argv[0];
        String tomcatConfigFile = argv[1];
        Properties globalProperties = new Properties();
        try (InputStream globalPropertiesInputStream = new FileInputStream(globalPropertiesFile)) {
            globalProperties.load(globalPropertiesInputStream);
        }
        InitScriptMain main = new InitScriptMain(System.getenv(), (Map) globalProperties);
        main.process();
        try (OutputStream globalPropertiesOutputStream = new FileOutputStream(globalPropertiesFile)) {
            globalProperties.store(globalPropertiesOutputStream, null);
        }
        try (PrintStream tomcatConfigOutputStream = new PrintStream(new FileOutputStream(tomcatConfigFile))) {
            String javaOpts = main.getJavaOptions().stream().collect(Collectors.joining(" "));
            tomcatConfigOutputStream.println("JAVA_OPTS=\"" + javaOpts + "\"");
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
        javaOptions.add(environment.getOrDefault("JAVA_OPTS", ""));
    }

    private void setGlobalOptionFromEnvironment(String option, String environmentVariable, String defaultValue) {
        globalProperties.put(option, environment.getOrDefault(environmentVariable, defaultValue));
    }

    private void setGlobalOptionFromEnvironment(String option, String environmentVariable) {
        globalProperties.put(option, environment.get(environmentVariable));
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
                "jdbc:postgresql://" + environment.get("DB_HOST") + ":" + environment.get("DB_PORT") + "/" + "DB_NAME");
        setGlobalOptionFromEnvironment("db.pool.validate.query", "DB_QUERY", "select 1");

        // Solr configuration
        String solrVersion = environment.computeIfAbsent("INDEX", k -> getDefaultSolrVersion());
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

        if (environment.getOrDefault("SOLR_SSL", "").equalsIgnoreCase("none") && alfrescoVersion.isGreaterThan("5.0")) {
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
        if (alfrescoVersion.isLessThan("6.0") && environment.getOrDefault("ALFRESCO_FLAVOR", "").equals("community")) {
            globalProperties.put("ooo.enabled", "true");
            globalProperties.put("jodconverter.enabled", "false");
        } else {
            globalProperties.put("ooo.enabled", "false");
            globalProperties.put("jodconverter.enabled", "true");
        }

        if(alfrescoVersion.isLessThan("6.0")) {
            globalProperties.put("ooo.exe", "/usr/lib/libreoffice/program/soffice");
            globalProperties.put("jodconverter.officeHome", "/usr/lib/libreoffice/");
            globalProperties.put("img.exe", "/usr/bin/convert");
            globalProperties.put("img.root", "/etc/ImageMagick");
            globalProperties.put("img.dyn", "/usr/lib");
            globalProperties.put("swf.exe", "/usr/bin/pdf2swf");
            globalProperties.put("alfresco-pdf-renderer.root", "/opt/alfresco");
            globalProperties.put("alfresco-pdf-renderer.exe", "${alfresco-pdf-renderer.root}/alfresco-pdf-renderer");
        }

        javaOptions.add("-Xms" + environment.getOrDefault("JAVA_XMS", "2048M"));
        javaOptions.add("-Xmx" + environment.getOrDefault("JAVA_XMX", "2048M"));
        javaOptions.add("-Dfile.encoding=UTF-8");

        if (environment.getOrDefault("JMX_ENABLED", "false").equalsIgnoreCase("true")) {
            javaOptions.add("-Dcom.sun.management.jmxremote.authenticate=false");
            javaOptions.add("-Dcom.sun.management.jmxremote.local.only=false");
            javaOptions.add("-Dcom.sun.management.jmxremote.ssl=false");
            javaOptions.add("-Dcom.sun.management.jmxremote");
            javaOptions.add("-Dcom.sun.management.jmxremote.rmi.port=5000");
            javaOptions.add("-Dcom.sun.management.jmxremote.port=5000");
            javaOptions.add("-Djava.rmi.server.hostname=" + environment.getOrDefault("JMX_RMI_HOST", "0.0.0.0"));
            globalProperties.put("alfresco.jmx.connector.enabled", "true");
        }

        if (environment.getOrDefault("DEBUG", "false").equalsIgnoreCase("true")) {
            javaOptions.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8000");
        }

        // Special handling for alfresco 4 - set MaxPermSize
        if (alfrescoVersion.major == 4) {
            javaOptions.add("-XX:MaxPermSize=256m");
        }

        if (alfrescoVersion.isGreaterThanOrEqual("6.1")) {
            globalProperties.put("messaging.subsystem.autoStart", "false");
        }

        environment.forEach((key, value) -> {
            if (key.startsWith("GLOBAL_")) {
                String globalProperty = key.substring("GLOBAL_".length());
                globalProperties.put(globalProperty, value);
            }
        });

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
