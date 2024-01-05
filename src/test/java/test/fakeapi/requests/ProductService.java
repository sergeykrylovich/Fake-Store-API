package test.fakeapi.requests;


import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import test.fakeapi.assertions.AssertableResponse;
import test.fakeapi.pojo.CreateProductPOJO;
import test.fakeapi.pojo.ProductsPOJO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static test.fakeapi.data.ProductsData.getRandomProduct;
import static test.fakeapi.specs.FakeStoreAPISpecs.prepareRequest;

public class ProductService {
    public static final String PRODUCT_BASEPATH = "/products";
    public static final String PRODUCTS_JSON_SCHEMA = "products-json-schema.json";

    @Step(value = "Get all products")
    public AssertableResponse getAllProducts() {

        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .when()
                .get()
                .then());
    }

    @Step(value = "get single product by product id")
    public JsonPath getSingleProduct(Object productId, String bearerToken, int statusCode) {

        return given()
                .filters(new AllureRestAssured())
                .spec(prepareRequest(PRODUCT_BASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .get("/" + productId)
                .then()
                .statusCode(statusCode)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PRODUCTS_JSON_SCHEMA))
                .extract().jsonPath();
    }

    @Step(value = "get single product by product id")
    public AssertableResponse getSingleProduct(int productId) {

        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .pathParam("productId", productId)
                .when()
                .get("/{productId}")
                .then());
    }

    @Step(value = "create product with arguments")
    public ProductsPOJO createProduct(String title, Integer price,
                                      String description, Integer categoryId,
                                      List<String> images, String bearerToken) {

        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, categoryId, images);
        return given()
                .filters(new AllureRestAssured())
                .spec(prepareRequest(PRODUCT_BASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .body(createProductPOJO)
                .when()
                .post("/")
                .then()
                .statusCode(SC_CREATED)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PRODUCTS_JSON_SCHEMA))
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    @Step(value = "create product without arguments")
    public AssertableResponse createRandomProduct(String token) {

        ProductsPOJO product = getRandomProduct();
        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .auth().oauth2(token)
                .body(product)
                .when()
                .post("/")
                .then());
    }

    @Step(value = "update product with arguments")
    public ProductsPOJO updateProduct(String title, Integer price,
                                      String description,
                                      List<String> images, Integer productId, String bearerToken) {

        CreateProductPOJO createProductPOJO = new CreateProductPOJO(title, price, description, images);

        return given()
                .filters(new AllureRestAssured())
                .spec(prepareRequest(PRODUCT_BASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .body(createProductPOJO)
                .when()
                .put("/" + productId)
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PRODUCTS_JSON_SCHEMA))
                .extract().jsonPath().getObject("", ProductsPOJO.class);
    }

    @Step(value = "delete product by product id")
    public ExtractableResponse<Response> deleteSingleProduct(Integer productId, String bearerToken, int statusCode) {

        return given()
                .filters(new AllureRestAssured())
                .spec(prepareRequest(PRODUCT_BASEPATH))
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .delete("/" + productId)
                .then()
                .log().body()
                .statusCode(statusCode)
                .extract();
    }

}
