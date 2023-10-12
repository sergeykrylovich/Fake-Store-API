package test.fakeapi.requests;


import test.fakeapi.pojo.ProductsPOJO;


import java.util.List;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestProducts {
    public final String BASEPATH = "/products";


    public List<ProductsPOJO> getAllProducts() {

        installSpecification(requestSpecification(BASEPATH), responseSpecification200());

        return given()
                .when()
                .get()
                .then()
                .log().all()
                .extract().jsonPath().getList("", ProductsPOJO.class);
    }

}
