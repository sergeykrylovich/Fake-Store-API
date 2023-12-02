package test.fakeapi.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class FakeStoreAPISpecs {
    public static final String BASEURL = "https://api.escuelajs.co/api/v1";
    public static final String MESSAGENOTFOUND = "Could not find any entity of type";
    public static final String MESSAGEFAILED = "Validation failed (numeric string is expected)";
    public static final String ERRORREQUEST = "Bad Request";
    public static final String NAMENOTFOUND = "EntityNotFoundError";
    public static final String PATH = "/api/v1";



    public static RequestSpecification requestSpecification(String BasePath) {

        return new RequestSpecBuilder()
                .setBaseUri(BASEURL)
                .setBasePath(BasePath)
                .setContentType("application/json; charset=utf-8")
                .build();
    }

}
