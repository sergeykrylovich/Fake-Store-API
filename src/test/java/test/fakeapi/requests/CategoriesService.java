package test.fakeapi.requests;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import test.fakeapi.assertions.AssertableResponse;
import test.fakeapi.pojo.CategoryPOJO;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static test.fakeapi.data.CategoryData.getRandomCategory;
import static test.fakeapi.specs.FakeStoreAPISpecs.prepareRequest;

public class CategoriesService {

    public static final String CATEGORY_BASEPATH = "/categories";
    public static final String CATEGORY_JSON_SCHEMA = "categories-json-schema.json";

    @Step(value = "Get all categories")
    public AssertableResponse getAllCategories() {

        return new AssertableResponse(given(prepareRequest(CATEGORY_BASEPATH))
                .when()
                .get()
                .then());
    }

    @Step(value = "Get all categories")
    public AssertableResponse getAllCategories(String token) {

        return new AssertableResponse(given(prepareRequest(CATEGORY_BASEPATH))
                .auth().oauth2(token)
                .when()
                .get()
                .then());
    }

    @Step(value = "Get a single category by category ID")
    public AssertableResponse getSingleCategory(int categoryId, String token) {

        return new AssertableResponse(given(prepareRequest(CATEGORY_BASEPATH))
                .auth().oauth2(token)
                .pathParam("categoryId", categoryId)
                .when()
                .get("/{categoryId}")
                .then());
    }

    @Step(value = "Get a single category by category ID")
    public AssertableResponse getSingleCategory(String categoryId, String token) {

        return new AssertableResponse(given(prepareRequest(CATEGORY_BASEPATH))
                .auth().oauth2(token)
                .pathParam("categoryId", categoryId)
                .when()
                .get("/{categoryId}")
                .then());
    }

    @Step(value = "Create category with name and image arguments")
    public JsonPath createCategory(String name, String image) {

        HashMap<String, String> bodyForCreateCategory = new HashMap<>();
        bodyForCreateCategory.put("name", name);
        bodyForCreateCategory.put("image", image);

        return given()
                .filters(new AllureRestAssured())
                .spec(prepareRequest(CATEGORY_BASEPATH))
                .body(bodyForCreateCategory)
                .when()
                .post("/")
                .then()
                .statusCode(201)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CATEGORY_JSON_SCHEMA))
                .extract()
                .jsonPath();
    }

    @Step(value = "Create category with category object")
    public AssertableResponse createCategory(CategoryPOJO category, String token) {

        return new AssertableResponse(given(prepareRequest(CATEGORY_BASEPATH))
                .auth().oauth2(token)
                .body(category)
                .when()
                .post("/")
                .then());
    }

    @Step(value = "Create category with name and image arguments")
    public AssertableResponse createCategory(CategoryPOJO category) {

        //installSpecification(requestSpecification(CATEGORYBASEPATH), responseSpecification1(201, categorySchema.get()));

/*        HashMap<String, String> bodyForCreateCategory = new HashMap<>();
        bodyForCreateCategory.put("name", name);
        bodyForCreateCategory.put("image", image);*/

        return new AssertableResponse(given(prepareRequest(CATEGORY_BASEPATH))
                .body(category)
                .when()
                .post("/")
                .then());
    }

    @Step(value = "Update data of category with id, name  and image arguments")
    public AssertableResponse updateCategory(int categoryId, CategoryPOJO category, String token) {

        return new AssertableResponse(given(prepareRequest(CATEGORY_BASEPATH))
                .auth().oauth2(token)
                .pathParam("categoryId", categoryId)
                .body(category)
                .when()
                .put("/{categoryId}")
                .then());
    }

    @Step(value = "Delete category by category ID")
    public AssertableResponse deleteCategory(int categoryId, String token) {

        return new AssertableResponse(given(prepareRequest(CATEGORY_BASEPATH))
                .auth().oauth2(token)
                .pathParam("categoryId", categoryId)
                .when()
                .delete("/{categoryId}")
                .then());
    }

    @Step(value = "Get all products by selected category")
    public AssertableResponse getAllProductsByCategory(int categoryId, String token) {

        return new AssertableResponse(given(prepareRequest(CATEGORY_BASEPATH))
                .auth().oauth2(token)
                .pathParam("categoryId", categoryId)
                .when()
                .get("/{categoryId}/products")
                .then());
    }

    @Step(value = "Create category random category")
    public CategoryPOJO createRandomCategory(String token) {

        return createCategory(getRandomCategory(),token).extractAs(CategoryPOJO.class);
    }

    public Integer getRandomCategoryId() {
        Random random  = new Random();
        List<Integer> categroiesIdList =  getAllCategories()
                .asList(CategoryPOJO.class)
                .stream()
                .map(CategoryPOJO::getId)
                .toList();

        int randomIdOfCategory = random.nextInt(categroiesIdList.size() - 1);

        return categroiesIdList.get(randomIdOfCategory);
    }


}
