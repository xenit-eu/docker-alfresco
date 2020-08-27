package eu.xenit.docker.alfresco;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class InitScriptMainTest {

    @Parameter(0)
    public String alfrescoVersion;
    @Parameter(1)
    public String alfrescoFlavor;

    @Parameters(name = "Alfresco {0}-{1}")
    public static Collection<String[]> getVersionsAndFlavors() {
        Collection<String[]> versions = new ArrayList<>();
        String[] alfrescoVersions = {"4.2", "5.0", "5.1", "5.2", "6.0", "6.1"};
        String[] alfrescoFlavors = {"enterprise", "community"};

        for (String alfrescoVersion : alfrescoVersions) {
            for (String alfrescoFlavor : alfrescoFlavors) {
                versions.add(new String[]{alfrescoVersion, alfrescoFlavor});
            }
        }

        return versions;
    }

    private Map<String, String> getDefaultEnvironment() {
        Map<String, String> env = new HashMap<>();

        env.put("ALFRESCO_VERSION", alfrescoVersion);
        env.put("ALFRESCO_FLAVOR", alfrescoFlavor);
        env.put("DB_HOST", "postgresql");
        env.put("DB_PORT", "5432");
        env.put("DB_NAME", "alfresco");
        env.put("SOLR_SSL", "https");

        return env;
    }

    private List<String> getPropertiesFiles(String testName) {
        List<String> propertiesFiles = new ArrayList<>();
        propertiesFiles.add(testName + "/default.properties");
        propertiesFiles.add(testName + "/" + alfrescoFlavor + ".properties");
        propertiesFiles.add(testName + "/" + alfrescoVersion + ".properties");
        propertiesFiles.add(testName + "/" + alfrescoVersion + "-" + alfrescoFlavor + ".properties");
        return propertiesFiles;
    }

    /**
     * Loads properties from
     * * defaults/default.properties
     * * defaults/&lt;alfrescoFlavor&gt;.properties
     * * defaults/&lt;alfrescoVersion&gt;.properties
     * * defaults/&lt;alfrescoVersion&gt;-&lt;alfrescoFlavor&lt;.properties
     * * &lt;testName&gt;/default.properties
     * * &lt;testName&gt;/&lt;alfrescoFlavor&gt;.properties
     * * &lt;testName&gt;/&lt;alfrescoVersion&gt;.properties
     * * &lt;testName&gt;/&lt;alfrescoVersion&gt;-&lt;alfrescoFlavor&lt;.properties
     * Property values are inherited along this structure to reduce duplication in properties
     */
    private Map<String, String> loadProperties(String testName) throws IOException {
        Map<String, String> allProperties = new HashMap<>();
        List<String> propertiesFiles = new ArrayList<>();
        propertiesFiles.addAll(getPropertiesFiles("defaults"));
        propertiesFiles.addAll(getPropertiesFiles(testName));
        for (String propertiesFile : propertiesFiles) {
            Properties properties = new Properties();
            try (InputStream propertiesInputStream = getClass().getResourceAsStream(propertiesFile)) {
                if (propertiesInputStream != null) {
                    properties.load(propertiesInputStream);
                }
                allProperties.putAll((Map) properties);
            }
        }

        Set<String> propertiesToDelete = new HashSet<>();
        for (Entry<String, String> entry : allProperties.entrySet()) {
            if (entry.getValue().equals("**delete**")) {
                propertiesToDelete.add(entry.getKey());
            }
        }

        for (String property : propertiesToDelete) {
            allProperties.remove(property);
        }

        return allProperties;
    }

    private InitScriptMain checkProperties(String testName, Map<String, String> environment) throws IOException {
        Map<String, String> globalProperties = new HashMap<>();
        InitScriptMain main = new InitScriptMain(environment, globalProperties, new HashMap<String, String>());
        main.process();
        Map<String, String> expectedProperties = loadProperties(testName);
        mapDifference(expectedProperties, globalProperties);
        return main;
    }

    private InitScriptMain checkLoggers(String testName, Map<String, String> environment) throws IOException {
        Map<String, String> log4jProperties = new HashMap<>();
        InitScriptMain main = new InitScriptMain(environment, new HashMap<String, String>(), log4jProperties);
        main.process();
        // load properties
        Map<String, String> expectedProperties;
        String filename = testName + "/default.properties";
        try (InputStream propertiesInputStream = getClass().getResourceAsStream(filename)) {
            Properties props = new Properties();
            if (propertiesInputStream != null) {
                props.load(propertiesInputStream);
            }
            expectedProperties = (Map) props;
        }
        // comparison
        mapDifference(expectedProperties, log4jProperties);
        return main;
    }

    private void mapDifference(Map<String, String> expected, Map<String, String> actual) {
        try {
            assertEquals(expected, actual);
        } catch (AssertionError e) {
            Set<String> expectedKeys = expected.keySet();
            Set<String> actualKeys = actual.keySet();

            Set<String> missingKeys = new HashSet<>(expectedKeys);
            missingKeys.removeAll(actualKeys);

            Set<String> unexpectedKeys = new HashSet<>(actualKeys);
            unexpectedKeys.removeAll(expectedKeys);

            String message = "\nMissing keys: " + missingKeys + "\nUnexpected keys: " + unexpectedKeys;

            Set<String> intersectionKeys = new HashSet<>(actualKeys);
            intersectionKeys.retainAll(expectedKeys);

            for (String intersectionKey : intersectionKeys) {
                if (actual.get(intersectionKey).equals(expected.get(intersectionKey))) {
                    actual.remove(intersectionKey);
                    expected.remove(intersectionKey);
                }
            }

            message += "\nBad values: Expected: <" + expected + ">, but was: <" + actual + ">";

            throw new AssertionError(message, e);
        }
    }

    @Test
    public void testDefaultSettings() throws IOException {
        Map<String, String> env = getDefaultEnvironment();
        InitScriptMain main = checkProperties("testDefaultSettings", env);
        testDefaultSSLSettings(main);
    }

    private void testDefaultSSLSettings(InitScriptMain main) {
        assertTrue("JAVA_OPTS contains default values for SSL store configuration",
                main.getJavaOptions().containsAll(Arrays.asList(
                        "-DTOMCAT_SSL_KEYSTORE=/opt/alfresco/keystore/ssl.keystore",
                        "-DTOMCAT_SSL_KEYSTORE_PASSWORD=kT9X6oe68t",
                        "-DTOMCAT_SSL_TRUSTSTORE=/opt/alfresco/keystore/ssl.truststore",
                        "-DTOMCAT_SSL_TRUSTSTORE_PASSWORD=kT9X6oe68t"
                )));
    }

    @Test
    public void testSetSolr4Explicitly() throws IOException {
        Map<String, String> env = getDefaultEnvironment();
        env.put("INDEX", "solr4");
        checkProperties("testSetSolr4Explicitly", env);
    }

    @Test
    public void testSetSolr6Explicitly() throws IOException {
        Map<String, String> env = getDefaultEnvironment();
        env.put("INDEX", "solr6");
        checkProperties("testSetSolr6Explicitly", env);
    }

    @Test
    public void testInheritsJavaOpts() throws IOException {
        Map<String, String> env = getDefaultEnvironment();
        env.put("JAVA_OPTS", "-Djava.opts.test=true");
        InitScriptMain main = checkProperties("testInheritsJavaOpts", env);

        assertTrue("JAVA_OPTS contains previously set property",
                main.getJavaOptions().contains("-Djava.opts.test=true"));
    }


    @Test
    public void testSetCustomGlobalProperties() throws IOException {
        Map<String, String> env = getDefaultEnvironment();
        env.put("GLOBAL_test.global.property", "value");
        env.put("GLOBAL_test.*.property", "value2");
        checkProperties("testSetCustomGlobalProperties", env);
    }

    @Test
    public void testSetCustomLoggerLevels() throws IOException {
        Map<String, String> env = getDefaultEnvironment();
        env.put("LOG4J_logger.eu.xenit.test", "DEBUG");
        checkLoggers("testSetCustomLoggerLevels", env);
    }

    @Test
    public void testSetCustomDirKeystore() throws IOException {
        Map<String, String> env = getDefaultEnvironment();
        String customKeystoreDir = "/ssl/keystores";
        env.put("DIR_KEYSTORE", customKeystoreDir);
        InitScriptMain main = new InitScriptMain(env, new HashMap<String, String>(), new HashMap<String, String>());
        main.process();
        assertTrue("JAVA_OPTS contains values for custom key- and truststore locations.",
                main.getJavaOptions().containsAll(
                        Arrays.asList("-DTOMCAT_SSL_KEYSTORE=".concat(customKeystoreDir).concat("/ssl.keystore"),
                                "-DTOMCAT_SSL_TRUSTSTORE=".concat(customKeystoreDir).concat("/ssl.truststore"))));
    }

    @Test
    public void testSetCustomKeystores() throws IOException {
        Map<String, String> env = getDefaultEnvironment();
        String methodname = "testSetCustomKeystores";
        String testKeystoreLocation = methodname.concat("/");
        List<String> keystorePasswordJavaOpts = new ArrayList<>();
        keystorePasswordJavaOpts.add("-DTOMCAT_SSL_KEYSTORE_PASSWORD=keystore");
        keystorePasswordJavaOpts.add("-DTOMCAT_SSL_TRUSTSTORE_PASSWORD=truststore");
        env.put("DIR_KEYSTORE", getClass().getResource(methodname).getPath());
        env.put("CUSTOM_KEYSTORES", "true");
        InitScriptMain main = new InitScriptMain(env, new HashMap<String, String>(), new HashMap<String, String>());
        main.process();
        assertTrue("JAVA_OPTS contains custom values for key- and truststore passwords.",
                main.getJavaOptions().containsAll(keystorePasswordJavaOpts));
    }

    private String getKeystorePasswordFromProps(String pathToPropsFile) throws IOException {
        Properties properties = new Properties();
        try (InputStream propertiesInputStream = getClass().getResourceAsStream(pathToPropsFile)) {
            if (propertiesInputStream != null) {
                properties.load(propertiesInputStream);
            }
        }
        return (String) properties.get("keystore.password");
    }

}
