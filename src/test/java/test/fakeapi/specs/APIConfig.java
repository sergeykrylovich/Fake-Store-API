package test.fakeapi.specs;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config.properties"})
public interface APIConfig extends Config {

    @Key("baseUrl")
    String baseUrl();

    @Key("path")
    String path();
}
