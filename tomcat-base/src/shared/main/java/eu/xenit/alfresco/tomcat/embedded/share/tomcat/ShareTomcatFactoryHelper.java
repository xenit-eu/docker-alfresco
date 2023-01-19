package eu.xenit.alfresco.tomcat.embedded.share.tomcat;

import eu.xenit.alfresco.tomcat.embedded.share.config.ShareConfiguration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ShareTomcatFactoryHelper {
    private ShareTomcatFactoryHelper() {
    }

    public static void createShareConfigCustomFile(ShareConfiguration shareConfiguration) {
        try {
            Path inputPath = Paths.get(shareConfiguration.getShareConfigTemplateFile());
            if (Files.exists(inputPath)) {
                Path classesDir = Paths.get(shareConfiguration.getTomcatConfiguration().getGeneratedClasspathDir(),
                        shareConfiguration.getShareConfigPath());
                Files.createDirectories(classesDir);
                Path tempProps = classesDir.resolve("share-config-custom.xml");
                if (Files.exists(tempProps)) {
                    Files.delete(tempProps);
                }
                tempProps = Files.createFile(tempProps);
                try (FileChannel sourceChannel = new FileInputStream(inputPath.toFile()).getChannel();
                     FileChannel destChannel = new FileOutputStream(tempProps.toFile()).getChannel()) {
                    destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
