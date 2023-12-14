package test.fakeapi.configs;

import io.qameta.allure.Step;
import org.aeonbits.owner.ConfigFactory;

public class ConfigSet {

    private static Configurable config = setConfig();


    private static Configurable setConfig() {
        if(System.getProperty("type") != null) {
            ConfigFactory.setProperty("fileConfig", System.getProperty("type"));
        }
        config = ConfigFactory.create(Configurable.class);
        return config;
    }


    public static String getBaseUrl() {
        return config.baseUrl();
    }

    public static String getPath() {
        return config.path();
    }

}
