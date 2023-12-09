package test.fakeapi.specs;

import io.qameta.allure.Step;
import org.aeonbits.owner.ConfigFactory;

public class Constants {

    private static APIConfig apiConfig = getProperty();
    public static final String BASE_URL = apiConfig.baseUrl();
    public static final String PATH = apiConfig.path();

    public static final String NOT_FIND_ANY_ENTITY_OF_TYPE = "Could not find any entity of type";
    public static final String NUMERIC_STRING_IS_EXPECTED = "Validation failed (numeric string is expected)";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String NOT_FOUND_ERROR = "EntityNotFoundError";


    @Step("Get a property")
    public static APIConfig getProperty() {
        ConfigFactory.setProperty("fileConfig", System.getProperty("type"));
        APIConfig config = ConfigFactory.create(APIConfig.class);
        System.out.println(config.path());
        System.out.println(config.baseUrl());
        return config;
    }
}
