package test.fakeapi.requests;

import io.restassured.path.json.JsonPath;
 import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import test.fakeapi.pojo.ProductsPOJO;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestCategories {

    public static final String CATEGORYBASEPATH = "/categories";
    public static final String CATEGORYSCHEMA = "categories-json-schema.json";
    static ThreadLocal<String> categorySchema = ThreadLocal.withInitial(() -> CATEGORYSCHEMA);





    public static JsonPath getAllCategories() {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200, CATEGORYSCHEMA));

        return given()
                .when()
                .get()
                .then()
                .extract()
                .jsonPath();
    }

    public static JsonPath getSingleCategory(int categoryId) {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200, CATEGORYSCHEMA));

        return given()
                .when()
                .get("/" + categoryId)
                .then()
                .extract()
                .jsonPath();
    }

    public static JsonPath createCategory(String name, String image) {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(201, categorySchema.get()));

        HashMap<String, String> bodyForCreateCategory = new HashMap<>();
        bodyForCreateCategory.put("name", name);
        bodyForCreateCategory.put("image", image);

        return given()
                .body(bodyForCreateCategory)
                .when()
                .post("/")
                .then()
                .extract()
                .jsonPath();
    }

    public static JsonPath updateCategory(int categoryId, String name, String image) {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200, categorySchema.get()));

        HashMap<String, String> bodyForCreateCategory = new HashMap<>();
        bodyForCreateCategory.put("name", name);
        bodyForCreateCategory.put("image", image);

        return given()
                .body(bodyForCreateCategory)
                .when()
                .put("/" + categoryId)
                .then()
                .extract()
                .jsonPath();
    }
    public static ExtractableResponse<Response> deleteCategory(int categoryId) {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200));

        return given()
                .when()
                .delete("/" + categoryId)
                .then()
                .extract();
    }

    public static List<ProductsPOJO> getAllProductsByCategory(int categoryId) {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200, RequestProducts.PRODUCTSSCHEMA));

        return given()
                .when()
                .get("/" + categoryId + "/products")
                .then()
                .extract()
                .jsonPath().getList("", ProductsPOJO.class);
    }


}
