package eu.xenit.docker.alfresco;

import java.util.HashMap;
import java.util.Map;

public class KeystoreDefaults {

    private String password;
    private final Map<String, AliasDefaults> aliases = new HashMap<>();

    public String getPassword() {
        return password;
    }

    private KeystoreDefaults withPassword(String password) {
        this.password = password;
        return this;
    }

    public Map<String, AliasDefaults> getAliases() {
        return aliases;
    }

    private KeystoreDefaults withAlias(String id, AliasDefaults aliasDefaults) {
        this.aliases.put(id, aliasDefaults);
        return this;
    }

    public static KeystoreDefaults METADATA_KEYSTORE = new KeystoreDefaults()
            .withPassword("mp6yc0UD9e")
            .withAlias("metadata", new AliasDefaults().withAlgorithm("DESede").withPassword("oKIWzVdEdA"));

    public static KeystoreDefaults SSL_KEYSTORE = new KeystoreDefaults()
            .withPassword("kT9X6oe68t")
            .withAlias("ssl.alfresco.ca", new AliasDefaults().withPassword("kT9X6oe68t"))
            .withAlias("ssl.repo", new AliasDefaults().withPassword("kT9X6oe68t"));

    public static KeystoreDefaults SSL_TRUSTSTORE = new KeystoreDefaults()
            .withPassword("kT9X6oe68t")
            .withAlias("alfresco.ca", new AliasDefaults().withPassword("kT9X6oe68t"));

    public static class AliasDefaults {

        private String keyData;
        private String algorithm;
        private String password;

        public String getKeyData() {
            return keyData;
        }

        private AliasDefaults withKeyData(String keyData) {
            this.keyData = keyData;
            return this;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        private AliasDefaults withAlgorithm(String algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public String getPassword() {
            return password;
        }

        private AliasDefaults withPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
