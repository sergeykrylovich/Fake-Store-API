package test.fakeapi.specs;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:${fileConfig}.properties"})
public interface APIConfig extends Config {

    @DefaultValue("https://api.escuelajs.co/api/v1")
    @Key("baseUrl")
    String baseUrl();

    @DefaultValue("/api/v1")
    @Key("path")
    String path();
}
