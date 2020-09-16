package eu.xenit.docker.alfresco;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class InitScriptMain {

    public static final String LOG4J_CONFIG_FILE = "LOG4J_CONFIG_FILE";

    public static void main(String[] argv) throws IOException {
        String globalPropertiesFile = argv[0];
        String tomcatConfigFile = argv[1];
        String log4jConfigFile = argv[2];
        Properties globalProperties = new Properties();
        Properties log4jConfig = new Properties();
        try (InputStream globalPropertiesInputStream = new FileInputStream(globalPropertiesFile)) {
            globalProperties.load(globalPropertiesInputStream);
        }
        if (Files.exists(Paths.get(log4jConfigFile))) {
            try (InputStream log4jConfigInputStream = new FileInputStream(log4jConfigFile)) {
                log4jConfig.load(log4jConfigInputStream);
            }
        } else {
             Files.createDirectories(Paths.get(log4jConfigFile).getParent());
        }
        InitScriptMain main = new InitScriptMain(System.getenv(), (Map) globalProperties, (Map) log4jConfig);
        main.process();
        try (OutputStream globalPropertiesOutputStream = new FileOutputStream(globalPropertiesFile)) {
            globalProperties.store(globalPropertiesOutputStream, null);
        }
        try (OutputStream log4jConfigOutputStream = new FileOutputStream(log4jConfigFile)) {
            log4jConfig.store(log4jConfigOutputStream, null);
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
    private final Map<String, String> log4jProperties;
    private final AlfrescoVersion alfrescoVersion;

    public List<String> getJavaOptions() {
        return Collections.unmodifiableList(javaOptions);
    }

    private final List<String> javaOptions = new ArrayList<>();

    public InitScriptMain(Map<String, String> environment, Map<String, String> globalProperties,
            Map<String, String> log4jProperties) {
        this.environment = new ThrowingMap<>(environment, "ENV");
        this.globalProperties = new ThrowingMap<>(globalProperties, "global-properties");
        this.log4jProperties = new ThrowingMap<>(log4jProperties, "log4j-properties");
        alfrescoVersion = AlfrescoVersion.parse(this.environment.get("ALFRESCO_VERSION"));
        if (this.environment.containsKey("JAVA_OPTS")) {
            javaOptions.add(this.environment.get("JAVA_OPTS"));
        }
    }

    private void setGlobalOptionFromEnvironment(String option, String environmentVariable, String defaultValue) {
        globalProperties.put(option, environment.containsKey(environmentVariable)?environment.get(environmentVariable):defaultValue);
    }

    public void process() throws IOException {
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
        setGlobalOptionFromEnvironment("db.host", "DB_HOST", "postgresql");
        setGlobalOptionFromEnvironment("db.port", "DB_PORT", "5432");
        setGlobalOptionFromEnvironment("db.name", "DB_NAME", "alfresco");
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
            globalProperties.put("solr.backup.alfresco.remoteBackupLocation", "/opt/alfresco-search-services/data/solr6Backup/alfresco");
            globalProperties.put("solr.backup.archive.remoteBackupLocation", "/opt/alfresco-search-services/data/solr6Backup/archive");
        }
        setGlobalOptionFromEnvironment("solr.host", "SOLR_HOST", "solr");
        setGlobalOptionFromEnvironment("solr.port", "SOLR_PORT", "8080");
        setGlobalOptionFromEnvironment("solr.port.ssl", "SOLR_PORT_SSL", "8443");
        setGlobalOptionFromEnvironment("solr.useDynamicShardRegistration", "DYNAMIC_SHARD_REGISTRATION", "false");
        setGlobalOptionFromEnvironment("solr.secureComms", "SOLR_SSL", "https");

        // SSL
        if ("none".equalsIgnoreCase(environment.get("SOLR_SSL")) && alfrescoVersion.isGreaterThan("5.0")) {
            // TODO: remove SSL connector from tomcat server.xml
        }

        String dirKeystore = environment.containsKey("DIR_KEYSTORE") ? environment.get("DIR_KEYSTORE") : "/opt/alfresco/keystore";
        boolean customKeystores = environment.containsKey("CUSTOM_KEYSTORES") && Boolean.parseBoolean(environment.get("CUSTOM_KEYSTORES"));
        Properties keystoreProperties = new Properties();
        Properties truststoreProperties = new Properties();
        if (customKeystores) {
            try (InputStream keystorePropertiesInputStream = new FileInputStream(dirKeystore.concat("/ssl-keystore-passwords.properties"))) {
                keystoreProperties.load(keystorePropertiesInputStream);
            }
            try (InputStream truststorePropertiesInputStream = new FileInputStream(dirKeystore.concat("/ssl-truststore-passwords.properties"))) {
                truststoreProperties.load(truststorePropertiesInputStream);
            }
        }
        // Get or default is not available in the interface in older versions, replaced it with a custom method.
        String keystorePassword = getKeystorePassword(keystoreProperties);
        String truststorePassword = getKeystorePassword(truststoreProperties);
        javaOptions.add("-DTOMCAT_SSL_KEYSTORE=".concat(dirKeystore.concat("/ssl.keystore")));
        javaOptions.add("-DTOMCAT_SSL_KEYSTORE_PASSWORD=".concat(keystorePassword));
        javaOptions.add("-DTOMCAT_SSL_TRUSTSTORE=".concat(dirKeystore.concat("/ssl.truststore")));
        javaOptions.add("-DTOMCAT_SSL_TRUSTSTORE_PASSWORD=".concat(truststorePassword));

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

        javaOptions.add("-Dfile.encoding=UTF-8");


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
            } else if (entry.getKey().startsWith("LOG4J_") && !LOG4J_CONFIG_FILE.equals(entry.getKey())) {
                String log4jProperty = entry.getKey().substring("LOG4J_".length());
                log4jProperties.put("log4j." + log4jProperty, entry.getValue());
            }
            // If you're about to add a third branch to this if-structure, consider abstracting it into a function?
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

    private String getKeystorePassword(Properties properties) {
        String password = (String) properties.get("keystore.password");
        if (password == null) {
            password = "kT9X6oe68t";
        }
        return password;
    }

}
