package eu.xenit.alfresco.tomcat.embedded.utils;

import java.util.function.Consumer;

public class ConfigurationHelper {
    private ConfigurationHelper() {
    }

    public static void setPropertyFromEnv(String env, Consumer<String> stringConsumer) {
        if (System.getenv(env) != null) {
            stringConsumer.accept(System.getenv(env));
        }
    }
}
