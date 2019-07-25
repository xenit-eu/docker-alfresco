package eu.xenit.docker.alfresco.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Properties;
import org.junit.Test;

public class InitScriptTest {
    private static final String ORIGINAL_GLOBAL_PROPERTIES_FILE = "src/main/resources/global/alfresco-global.properties";
    private static final String GLOBAL_PROPERTIES_FILE = "src/test/resources/alfresco-global.properties";
    private static final String ALFRESCO_INIT_SCRIPT = "src/main/resources/global/90-init-alfresco.sh";

    @Test
    public void testInitScriptWithDefaults() throws Exception {
        // copy the the alfresco-global.properties file which will be in the image
        copyPropertiesFile();

        // run the init script
        ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> env = pb.environment();
        env.put("CONFIG_FILE", GLOBAL_PROPERTIES_FILE);
        pb.command("/bin/bash",ALFRESCO_INIT_SCRIPT);
        Process p = pb.start();
        String output = loadStream(p.getInputStream());
        String error = loadStream(p.getErrorStream());
        int rc = p.waitFor();

        Properties prop = readProperties();
        // test some of the properties
        assertEquals("jdbc:postgresql://postgresql:5432/alfresco", prop.getProperty("db.url"));
        assertEquals("alfresco", prop.getProperty("db.username"));
        assertEquals("admin", prop.getProperty("db.password"));
    }

    @Test
    public void testInitScriptWithGlobalEnvironmentVariables() throws Exception {
        // copy the the alfresco-global.properties file which will be in the image
        copyPropertiesFile();

        // run the init script
        ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> env = pb.environment();
        env.put("CONFIG_FILE", GLOBAL_PROPERTIES_FILE);
        env.put("GLOBAL_db.url", "jdbc:postgresql://mypostgresql:5432/alfresco");
        env.put("GLOBAL_db.username", "myalfresco");
        env.put("GLOBAL_db.password", "mypassword");
        pb.command("/bin/bash",ALFRESCO_INIT_SCRIPT);
        Process p = pb.start();
        String output = loadStream(p.getInputStream());
        String error = loadStream(p.getErrorStream());
        int rc = p.waitFor();

        Properties prop = readProperties();
        // test some of the properties
        assertEquals("jdbc:postgresql://mypostgresql:5432/alfresco", prop.getProperty("db.url"));
        assertEquals("myalfresco", prop.getProperty("db.username"));
        assertEquals("mypassword", prop.getProperty("db.password"));
    }

    @Test
    public void testInitScriptWithEnvironmentVariables() throws Exception {
        // copy the the alfresco-global.properties file which will be in the image
        copyPropertiesFile();

        // run the init script
        ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> env = pb.environment();
        env.put("CONFIG_FILE", GLOBAL_PROPERTIES_FILE);
        env.put("DB_URL", "jdbc:postgresql://mypostgresql:5432/alfresco");
        env.put("DB_USERNAME", "myalfresco");
        env.put("DB_PASSWORD", "mypassword");
        pb.command("/bin/bash",ALFRESCO_INIT_SCRIPT);
        Process p = pb.start();
        String output = loadStream(p.getInputStream());
        String error = loadStream(p.getErrorStream());
        int rc = p.waitFor();

        assertEquals(0,rc);

        Properties prop = readProperties();
        // test some of the properties
        assertEquals("jdbc:postgresql://mypostgresql:5432/alfresco", prop.getProperty("db.url"));
        assertEquals("myalfresco", prop.getProperty("db.username"));
        assertEquals("mypassword", prop.getProperty("db.password"));
    }

    @Test
    public void testInitScriptWithGlobalEnvironmentVariablesRanTwice() throws Exception {
        // copy the the alfresco-global.properties file which will be in the image
        copyPropertiesFile();

        // run the init script twice
        ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> env = pb.environment();
        env.put("CONFIG_FILE", GLOBAL_PROPERTIES_FILE);
        final String ldapQuery = "(&(objectclass=user)(memberof:1.2.840.113556.1.4.1941:=CN=alfresco_synchronization,OU=Alfresco,OU=Security,OU=Groups,OU=REPOSITORY,DC=somecompany,DC=adms)(userAccountControl:1.2.840.113556.1.4.803:=512))";
        env.put("GLOBAL_ldap.synchronization.personQuery",
                ldapQuery);
        pb.command("/bin/bash", ALFRESCO_INIT_SCRIPT);

        for (int i = 0; i < 2; i++) {
            Process p = pb.start();
            String output = loadStream(p.getInputStream());
            String error = loadStream(p.getErrorStream());

 //           System.out.println("output=" + output);
            int rc = p.waitFor();

            assertEquals(0, rc);

            Properties prop = readProperties();
            // test some of the properties
            assertEquals(ldapQuery, prop.getProperty("ldap.synchronization.personQuery"));
        }
    }

    private static Properties readProperties() throws IOException {
        InputStream input = new FileInputStream(GLOBAL_PROPERTIES_FILE);

        Properties prop = new Properties();
        prop.load(input);

        return prop;
    }

    private static void copyPropertiesFile() throws IOException {
        Files.copy(Paths.get(ORIGINAL_GLOBAL_PROPERTIES_FILE), Paths.get(GLOBAL_PROPERTIES_FILE), StandardCopyOption.REPLACE_EXISTING);
    }

    private static String loadStream(InputStream s) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(s));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line=br.readLine()) != null)
            sb.append(line).append("\n");
        return sb.toString();
    }
}
