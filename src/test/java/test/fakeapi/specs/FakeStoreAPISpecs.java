package test.fakeapi.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;

public class FakeStoreAPISpecs {

    public static APIConfig apiConfig = ConfigFactory.create(APIConfig.class);
    public static final String BASEURL = apiConfig.baseUrl();
    public static final String MESSAGENOTFOUND = "Could not find any entity of type";
    public static final String MESSAGEFAILED = "Validation failed (numeric string is expected)";
    public static final String ERRORREQUEST = "Bad Request";
    public static final String NAMENOTFOUND = "EntityNotFoundError";
    public static final String PATH = apiConfig.path();



    public static RequestSpecification requestSpecification(String BasePath) {

        return new RequestSpecBuilder()
                .setBaseUri(BASEURL)
                .setBasePath(BasePath)
                .setContentType("application/json; charset=utf-8")
                .build();
    }

}
