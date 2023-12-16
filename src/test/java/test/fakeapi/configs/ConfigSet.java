package test.fakeapi.configs;

import org.aeonbits.owner.ConfigFactory;

public class ConfigSet {

    private static Configurable config = setConfig();


    private static Configurable setConfig() {
        if(System.getProperty("type") != null) {
            ConfigFactory.setProperty("fileConfig", System.getProperty("type"));
        }
        return ConfigFactory.create(Configurable.class);
    }


    public static Configurable getConfig() {
        return config;
    }

}
