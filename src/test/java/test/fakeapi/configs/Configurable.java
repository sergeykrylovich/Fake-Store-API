package test.fakeapi.configs;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.*;

@Sources({"classpath:{fileConfig}.properties"})
public interface Configurable extends Config {

    @DefaultValue("https://api.escuelajs.co/api/v1")
    @Key("baseUrl")
    String baseUrl();

    @DefaultValue("/api/v1")
    @Key("path")
    String path();
}
