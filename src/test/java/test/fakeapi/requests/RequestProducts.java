package test.fakeapi.requests;


import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import net.datafaker.Faker;
import test.fakeapi.pojo.CreateProductPOJO;
import test.fakeapi.pojo.ProductsPOJO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestProducts {
    public static final String PRODUCTBASEPATH = "/products";
    public static final String PRODUCTSSCHEMA = "products-json-schema.json";

    @Step(value = "get all products")
    public List<ProductsPOJO> getAllProducts(String bearerToken) {

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(PRODUCTBASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get()
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PRODUCTSSCHEMA))
                .extract().jsonPath().getList("", ProductsPOJO.class);
    }

    @Step(value = "get single product by product id")
    public JsonPath getSingleProduct(Object productId, String bearerToken, int statusCode) {

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(PRODUCTBASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/" + productId)
                .then()
                .statusCode(statusCode)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PRODUCTSSCHEMA))
                .extract().jsonPath();
    }

    @Step(value = "get single product by product id")
    public JsonPath getSingleProduct(int productId) {

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(PRODUCTBASEPATH))
                //.header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/" + productId)
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PRODUCTSSCHEMA))
                .extract().jsonPath();
    }

    @Step(value = "create product with arguments")
    public ProductsPOJO createProduct(String title, Integer price,
                                      String description, Integer categoryId,
                                      List<String> images, String bearerToken) {

        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, categoryId, images);
        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(PRODUCTBASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .body(createProductPOJO)
                .when()
                .post("/")
                .then()
                .statusCode(SC_CREATED)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PRODUCTSSCHEMA))
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    @Step(value = "create product without arguments")
    public ProductsPOJO createProductWithoutArgs(String bearerToken) {

        Faker faker = new Faker();
        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        Integer categoryId = faker.number().numberBetween(1, 5);
        List<String> images = List.of(faker.internet().image());

        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, categoryId, images);
        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(PRODUCTBASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .body(createProductPOJO)
                .when()
                .post("/")
                .then()
                .statusCode(SC_CREATED)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PRODUCTSSCHEMA))
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    @Step(value = "update product with arguments")
    public ProductsPOJO updateProduct(String title, Integer price,
                                      String description,
                                      List<String> images, Integer productId, String bearerToken) {

        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, images);

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(PRODUCTBASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .body(createProductPOJO)
                .when()
                .put("/" + productId)
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PRODUCTSSCHEMA))
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    @Step(value = "delete product by product id")
    public ExtractableResponse<Response> deleteSingleProduct(Integer productId, String bearerToken, int statusCode) {

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(PRODUCTBASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .delete("/" + productId)
                .then()
                .log().body()
                .statusCode(statusCode)
                .extract();
    }

}
