package test.fakeapi.requests;


import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import test.fakeapi.assertions.AssertableResponse;
import test.fakeapi.pojo.CreateProductPOJO;
import test.fakeapi.pojo.ProductsPOJO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
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

    @Step(value = "Get all products with token")
    public AssertableResponse getAllProducts(String token) {

        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .auth().oauth2(token)
                .when()
                .get()
                .then());
    }

    @Step(value = "Get all products with pagination")
    public AssertableResponse getAllProductsWithPagination(String token, int offset, int limit) {

        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .auth().oauth2(token)
                .pathParams("offset", offset, "limit", limit)
                .when()
                .get("?offset={offset}&limit={limit}")
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
    public AssertableResponse getSingleProduct(int productId, String token) {

        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .auth().oauth2(token)
                .pathParam("productId", productId)
                .when()
                .get("/{productId}")
                .then());
    }

    @Step(value = "get single product by product id")
    public AssertableResponse getSingleProduct(String productId, String token) {

        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .auth().oauth2(token)
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
    @Step(value = "create product with arguments")
    public AssertableResponse createProduct(ProductsPOJO product, String token) {

        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .auth().oauth2(token)
                .body(product)
                .when()
                .post("/")
                .then());
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
    public AssertableResponse updateProduct(Integer productId, ProductsPOJO product, String token) {

        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .auth().oauth2(token)
                .pathParam("productId", productId)
                .body(product)
                .when()
                .put("/{productId}")
                .then());
    }

    @Step(value = "delete product by product id")
    public AssertableResponse deleteSingleProduct(Integer productId, String token) {

        return new AssertableResponse(given(prepareRequest(PRODUCT_BASEPATH))
                .auth().oauth2(token)
                .pathParam("productId", productId)
                .when()
                .delete("/{productId}")
                .then());
    }

}
