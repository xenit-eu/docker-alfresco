package eu.xenit.alfresco.tomcat.embedded.alfresco;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AlfrescoFactory {
    private Configuration configuration;

    public AlfrescoFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public void InitAlfresco() {
        createAlfrescoFolder();
    }

    private Path createAlfrescoFolder() {
        if (configuration.getAlfrescoLocation() == null) return null;

        return createFolder(configuration.getAlfrescoLocation());
    }

    private Path createFolder(String pathString) {
        Path path = Paths.get(pathString);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                return null;
            }
        }

        return path;
    }
}
