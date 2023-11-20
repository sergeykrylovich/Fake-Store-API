package test.fakeapi.requests;

import test.fakeapi.specs.FakeStoreAPISpecs;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestCategories {

    public static final String CATEGORYBASEPATH = "/categories";
    public static final String CATEGROYSCHEMA = "categories-json-schema.json";

    public void getAllCategories() {
        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200, CATEGROYSCHEMA));
        given()
                .when()
                .get()
                .then()
                .log().all();
    }


}
