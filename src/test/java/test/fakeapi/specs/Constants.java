package test.fakeapi.specs;

import test.fakeapi.configs.ConfigSet;

public class Constants {

    public static final String BASE_URL = ConfigSet.getConfig().baseUrl();
    public static final String PATH = ConfigSet.getConfig().path();

    public static final String NOT_FIND_ANY_ENTITY_OF_TYPE = "Could not find any entity of type";
    public static final String NUMERIC_STRING_IS_EXPECTED = "Validation failed (numeric string is expected)";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String NOT_FOUND_ERROR = "EntityNotFoundError";
    public static final String ADMIN_IS_NOT_FOR_DELETE = "This user is not available for deleting; instead, create your own user to delete.";

}
