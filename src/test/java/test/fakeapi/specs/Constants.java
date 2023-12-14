package test.fakeapi.specs;

import io.qameta.allure.Step;
import org.aeonbits.owner.ConfigFactory;
import test.fakeapi.configs.ConfigSet;
import test.fakeapi.configs.Configurable;

public class Constants {

    public static final String BASE_URL = ConfigSet.getBaseUrl();
    public static final String PATH = ConfigSet.getPath();

    public static final String NOT_FIND_ANY_ENTITY_OF_TYPE = "Could not find any entity of type";
    public static final String NUMERIC_STRING_IS_EXPECTED = "Validation failed (numeric string is expected)";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String NOT_FOUND_ERROR = "EntityNotFoundError";

}
