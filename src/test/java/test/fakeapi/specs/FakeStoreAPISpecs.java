package test.fakeapi.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class FakeStoreAPISpecs {


    public static RequestSpecification requestSpecification(String BasePath) {

        return new RequestSpecBuilder()
                .setBaseUri(Constants.BASE_URL)
                .setBasePath(BasePath)
                .setContentType("application/json; charset=utf-8")
                .build();
    }

}
