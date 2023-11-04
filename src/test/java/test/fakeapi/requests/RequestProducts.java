package test.fakeapi.requests;


import net.datafaker.Faker;
import test.fakeapi.pojo.CreateProductPOJO;
import test.fakeapi.pojo.ProductsPOJO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestProducts {
    public static final String BASEPATH = "/products";


    public List<ProductsPOJO> getAllProducts(String bearerToken) {

        installSpecification(requestSpecification(BASEPATH), responseSpecification(200));

        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get()
                .then()
                .extract().jsonPath().getList("", ProductsPOJO.class);
    }

    public ProductsPOJO getSingleProduct(int productId, String bearerToken) {

        installSpecification(requestSpecification(BASEPATH), responseSpecification(200));


        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/" + productId)
                .then()
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    public ProductsPOJO createProduct(String title, Integer price,
                                      String description, Integer categoryId,
                                      List<String> images, String bearerToken) {


        installSpecification(requestSpecification(BASEPATH), responseSpecification(201));


        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, categoryId, images);
        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(createProductPOJO)
                .when()
                .post("/")
                .then()
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }
    public ProductsPOJO createProductWithoutArgs(String bearerToken) {

        Faker faker = new Faker();
        installSpecification(requestSpecification(BASEPATH), responseSpecification(201));
        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        Integer categoryId = faker.number().numberBetween(1, 5);
        List<String> images= List.of(faker.internet().image());

        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, categoryId, images);
        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(createProductPOJO)
                .when()
                .post("/")
                .then()
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    public ProductsPOJO updateProduct(String title, Integer price,
                                      String description,
                                      List<String> images, Integer productId, String bearerToken) {
        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, null, images);

        installSpecification(requestSpecification(BASEPATH), responseSpecification(200));

        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .body(createProductPOJO)
                .when()
                .put("/" + productId)
                .then()
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    public String deleteSingleProduct(Integer productId, String bearerToken, int statusCode) {

        installSpecification(requestSpecification(BASEPATH), responseSpecification(statusCode));

        return given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .delete("/" + productId)
                .then()
                .extract().htmlPath().get("html.body");
    }

}
