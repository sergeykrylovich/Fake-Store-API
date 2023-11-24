package test.fakeapi.requests;

import io.restassured.path.json.JsonPath;
import jdk.security.jarsigner.JarSigner;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestCategories {

    public static final String CATEGORYBASEPATH = "/categories";
    public static final String CATEGORYSCHEMA = "categories-json-schema.json";

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

    public static JsonPath createCategory() {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(201, CATEGORYSCHEMA));

        HashMap<String, String> bodyForCreateCategory = new HashMap<>();
        bodyForCreateCategory.put("name", "Plane");
        bodyForCreateCategory.put("image", "https://placeimg.com/640/480/any");

        return given()
                .body(bodyForCreateCategory)
                .when()
                .post("/")
                .then()
                .extract()
                .jsonPath();
    }

    public static JsonPath updateCategory() {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(201, CATEGORYSCHEMA));

        HashMap<String, String> bodyForCreateCategory = new HashMap<>();
        bodyForCreateCategory.put("name", "Plane");
        bodyForCreateCategory.put("image", "https://placeimg.com/640/480/any");

        return given()
                .body(bodyForCreateCategory)
                .when()
                .post("/")
                .then()
                .extract()
                .jsonPath();
    }
    public static JsonPath deleteCategory(int categoryId) {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(201, CATEGORYSCHEMA));

        return given()
                .when()
                .delete("/" + categoryId)
                .then()
                .extract()
                .jsonPath();
    }

    public static JsonPath getAllProductsByCategory(int categoryId) {

        installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification(200, CATEGORYSCHEMA));

        return given()
                .when()
                .delete("/" + categoryId + "/products")
                .then()
                .extract()
                .jsonPath();
    }


}
