package eu.xenit.docker.alfresco;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        env.put("JAVA_XMS", "2048M");
        env.put("JAVA_XMX", "2048M");
        env.put("DEBUG", "false");
        env.put("JMX_ENABLED", "false");
        env.put("JMX_RMI_HOST", "0.0.0.0");
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
        return allProperties;
    }

    private void checkProperties(String testName, Map<String, String> environment) throws IOException {
        Map<String, String> globalProperties = new HashMap<>();
        InitScriptMain main = new InitScriptMain(environment, globalProperties);
        main.process();
        Map<String, String> expectedProperties = loadProperties(testName);
        mapDifference(expectedProperties, globalProperties);
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
                if(actual.get(intersectionKey).equals(expected.get(intersectionKey))) {
                    actual.remove(intersectionKey);
                    expected.remove(intersectionKey);
                }
            }

            message+="\nBad values: Expected: <"+expected+">, but was: <"+actual+">";


            throw new AssertionError(message, e);
        }
    }

    @Test
    public void testDefaultSettings() throws IOException {
        Map<String, String> env = getDefaultEnvironment();
        checkProperties("testDefaultSettings", env);
    }

}
