package test.fakeapi.specs;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class FakeStoreAPISpecs {
    private static final String BASEURL = "https://api.escuelajs.co/api/v1";



    public static RequestSpecification requestSpecification(String BasePath) {
        return new RequestSpecBuilder()
                .setBaseUri(BASEURL)
                .setBasePath(BasePath)
                .setContentType("application/json; charset=utf-8")
                .build();
    }

    public static ResponseSpecification responseSpecification200() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                //.expectContentType("application/json; charset=utf-8")
                .build();
    }
    public static ResponseSpecification responseSpecification201() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType("application/json; charset=utf-8")
                .build();
    }

    public static void installSpecification(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }

}
