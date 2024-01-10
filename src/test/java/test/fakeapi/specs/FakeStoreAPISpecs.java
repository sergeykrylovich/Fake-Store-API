package test.fakeapi.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class FakeStoreAPISpecs {


    public static RequestSpecification prepareRequest(String BasePath) {

        return new RequestSpecBuilder()
                //.setBaseUri(Constants.BASE_URL)
                .setBasePath(BasePath)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification prepareRequestForUploadFile(String BasePath) {

        return new RequestSpecBuilder()
                //.setBaseUri(Constants.BASE_URL)
                .setBasePath(BasePath)
                .setContentType("multipart/form-data")
                .build();
    }

}
