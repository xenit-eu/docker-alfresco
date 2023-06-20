package eu.xenit.alfresco.tomcat.embedded.share.tomcat;

import eu.xenit.alfresco.tomcat.embedded.share.config.ShareConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShareTomcatFactoryHelper {
    private ShareTomcatFactoryHelper() {
    }

    public static void createShareConfigCustomFile(ShareConfiguration shareConfiguration) {
        try {
            Path inputPath = Paths.get(shareConfiguration.getShareConfigTemplateFile());
            if (Files.exists(inputPath)) {
                Path classesDir = Paths.get(shareConfiguration.getTomcatConfiguration().getGeneratedClasspathDir(), shareConfiguration.getShareConfigPath());
                Files.createDirectories(classesDir);
                Path tempProps = Paths.get(shareConfiguration.getTomcatConfiguration().getGeneratedClasspathDir(), shareConfiguration.getShareConfigPath(), "share-config-custom.xml");
                if (Files.exists(tempProps)) {
                    Files.delete(tempProps);
                }
                tempProps = Files.createFile(tempProps);
                try (OutputStream os = Files.newOutputStream(tempProps);
                     PrintStream printStream = new PrintStream(os);
                     InputStream is = Files.newInputStream(inputPath);
                     InputStreamReader isr = new InputStreamReader(is);
                     BufferedReader br = new BufferedReader(isr)

                ) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        printStream.println(replaceWithEnvironmentVariables(line, shareConfiguration));
                    }

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String replaceWithEnvironmentVariables(String input, ShareConfiguration shareConfiguration) {
        String patternString = "\\$\\{[A-Za-z_]*}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll(matchResult -> {
            String match = matchResult.group();
            String envProp = match.substring(2, match.length() - 1);
            if (!envProp.isEmpty()) {
                String env = shareConfiguration.getValueOf(envProp);
                if (env != null && !env.isEmpty())
                    return env;
            }
            return "\\$\\{" + match.substring(2);
        });
    }
}
