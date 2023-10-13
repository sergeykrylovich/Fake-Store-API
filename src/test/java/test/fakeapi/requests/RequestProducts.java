package test.fakeapi.requests;


import test.fakeapi.pojo.CreateProductPOJO;
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

    public ProductsPOJO getSingleProduct(int productId) {

        installSpecification(requestSpecification(BASEPATH), responseSpecification200());

        return given()
                .when()
                .get("/" + productId)
                .then()
                .log().all()
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    public ProductsPOJO createProduct(String title, Integer price,
                                      String description, Integer categoryId,
                                      List<String> images) {

        installSpecification(requestSpecification(BASEPATH), responseSpecification201());

        return given()
                .body(new CreateProductPOJO(title, price, description, categoryId, images))
                .when()
                .post("/")
                .then()
                .log().all()
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }


}
