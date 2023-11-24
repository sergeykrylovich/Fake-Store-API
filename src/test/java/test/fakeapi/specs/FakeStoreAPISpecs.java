package test.fakeapi.specs;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

public class FakeStoreAPISpecs {
    private static final String BASEURL = "https://api.escuelajs.co/api/v1";



    public static RequestSpecification requestSpecification(String BasePath) {

        return new RequestSpecBuilder()
                .setBaseUri(BASEURL)
                .setBasePath(BasePath)
                .setContentType("application/json; charset=utf-8")
                .build();
    }

    public static ResponseSpecification responseSpecification(int statusCode, String jsonScheme) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectBody(JsonSchemaValidator.matchesJsonSchemaInClasspath(jsonScheme))
                .expectResponseTime(lessThan(4l), TimeUnit.SECONDS)
                .build();
    }
    public static ResponseSpecification responseSpecification(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectResponseTime(lessThan(4l), TimeUnit.SECONDS)
                .build();
    }

    public static void installSpecification(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

}
