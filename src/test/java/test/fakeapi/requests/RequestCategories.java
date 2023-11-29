package test.fakeapi.requests;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
 import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import test.fakeapi.pojo.ProductsPOJO;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestCategories {

    public static final String CATEGORYBASEPATH = "/categories";
    public static final String CATEGORYSCHEMA = "categories-json-schema.json";
    static ThreadLocal<String> categorySchema = ThreadLocal.withInitial(() -> CATEGORYSCHEMA);




    @Step(value = "Get all categories")
    public JsonPath getAllCategories() {

        //installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200, CATEGORYSCHEMA));

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(CATEGORYBASEPATH))
                .when()
                .get()
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CATEGORYSCHEMA))
                .extract()
                .jsonPath();
    }

    @Step(value = "Get a single category by category ID")
    public JsonPath getSingleCategory(int categoryId) {

       // installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200, CATEGORYSCHEMA));

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(CATEGORYBASEPATH))
                .when()
                .get("/" + categoryId)
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CATEGORYSCHEMA))
                .extract()
                .jsonPath();
    }

    @Step(value = "Create category with name and image arguments")
    public JsonPath createCategory(String name, String image) {

        //installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification1(201, categorySchema.get()));

        HashMap<String, String> bodyForCreateCategory = new HashMap<>();
        bodyForCreateCategory.put("name", name);
        bodyForCreateCategory.put("image", image);

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(CATEGORYBASEPATH))
                .body(bodyForCreateCategory)
                .when()
                .post("/")
                .then()
                .statusCode(201)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CATEGORYSCHEMA))
                .extract()
                .jsonPath();
    }

    @Step(value = "Update data of category with id, name  and image arguments")
    public JsonPath updateCategory(int categoryId, String name, String image) {

        //installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification1(200, categorySchema.get()));

        HashMap<String, String> bodyForCreateCategory = new HashMap<>();
        bodyForCreateCategory.put("name", name);
        bodyForCreateCategory.put("image", image);

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(CATEGORYBASEPATH))
                .body(bodyForCreateCategory)
                .when()
                .put("/" + categoryId)
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CATEGORYSCHEMA))
                .extract()
                .jsonPath();
    }

    @Step(value = "Delete category by category ID")
    public ExtractableResponse<Response> deleteCategory(int categoryId) {

        //installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200));

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(CATEGORYBASEPATH))
                .when()
                .delete("/" + categoryId)
                .then()
                .statusCode(SC_OK)
                .extract();
    }

    @Step(value = "Get all products by selected category")
    public List<ProductsPOJO> getAllProductsByCategory(int categoryId) {

        //installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200, RequestProducts.PRODUCTSSCHEMA));

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(CATEGORYBASEPATH))
                .when()
                .get("/" + categoryId + "/products")
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(RequestProducts.PRODUCTSSCHEMA))
                .extract()
                .jsonPath().getList("", ProductsPOJO.class);
    }

}
