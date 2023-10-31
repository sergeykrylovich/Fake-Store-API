package test.fakeapi.requests;


import test.fakeapi.pojo.CreateProductPOJO;
import test.fakeapi.pojo.ProductsPOJO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestProducts {
    public final String BASEPATH = "/products";


    public List<ProductsPOJO> getAllProducts(String bearerToken) {

        installSpecification(requestSpecification(BASEPATH), responseSpecification200());

        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get()
                .then()
                .log().all()
                .extract().jsonPath().getList("", ProductsPOJO.class);
    }

    public ProductsPOJO getSingleProduct(int productId, String bearerToken) {

        installSpecification(requestSpecification(BASEPATH), responseSpecification200());


        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .log().all()
                .when()
                .get("/" + productId)
                .then()
                .log().all()
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    public ProductsPOJO createProduct(String title, Integer price,
                                      String description, Integer categoryId,
                                      List<String> images, String bearerToken) {


        installSpecification(requestSpecification(BASEPATH), responseSpecification201());


        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, categoryId, images);
        return given()
                .header("Authorization", "Bearer " + bearerToken)
                //.log().all()
                .body(createProductPOJO)
                .when()
                .post("/")
                .then()
                //.log().all()
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    public ProductsPOJO updateProduct(String title, Integer price,
                                      String description,
                                      List<String> images, Integer productId, String bearerToken) {
        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, null, images);

        installSpecification(requestSpecification(BASEPATH), responseSpecification200());

        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(createProductPOJO)
                .when()
                .put("/" + productId)
                .then()
                .log().all()
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    public String deleteSingleProduct(Integer productId, String bearerToken) {


        installSpecification(requestSpecification(BASEPATH), responseSpecification200());


        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .delete("/" + productId)
                .then()
                .log().all()
                .extract().htmlPath().get("html.body");
    }


}
