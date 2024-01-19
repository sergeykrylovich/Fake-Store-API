package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.pojo.RecordNotFound;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.AuthService;
import test.fakeapi.requests.BaseApi;
import test.fakeapi.requests.CategoriesService;
import test.fakeapi.requests.UserService;
import test.fakeapi.specs.Constants;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.assertions.Conditions.*;
import static test.fakeapi.requests.CategoriesService.CATEGORY_BASEPATH;
import static test.fakeapi.requests.CategoriesService.CATEGORY_JSON_SCHEMA;
import static test.fakeapi.specs.Constants.*;


@Epic("API of Categories")
public class CategoriesTests extends BaseApi {

    CategoriesService categoriesService;

    private AuthService authService;
    private UserService userService;
    private String token;
    private Random random;

    @BeforeEach
    public void initTests() {
        categoriesService = new CategoriesService();
        authService = new AuthService();
        userService = new UserService();
        token = authService.createAndLoginRandomUser().getJWTToken();
    }

    @AfterEach
    public void cleanUp() {
        Integer id = authService.getUserByJWTToken(token)
                .extractAs(UserPOJO.class)
                .getId();

        userService.deleteUser(id);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("PositiveTest")
    @Tag("GetAllCategories")
    @DisplayName(value = "Get all categories")
    void getAllCategoriesTest() {

         List<CategoryPOJO> expectedCategories = categoriesService.getAllCategories(token)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(CATEGORY_JSON_SCHEMA))
                .should(hasResponseTime(5))
                .asList(CategoryPOJO.class);

        assertThat(expectedCategories).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("GetSingleCategory")
    @Tag("PositiveTest")
    @DisplayName("Get a non deletable single category")
    public void getSingleCategoryTest(int expectedCategoryId) {

        CategoryPOJO actualCategory = categoriesService.getSingleCategory(expectedCategoryId, token)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(CATEGORY_JSON_SCHEMA))
                .should(hasResponseTime(4))
                .extractAs(CategoryPOJO.class);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(actualCategory.getId()).isEqualTo(expectedCategoryId);
            assertThat(actualCategory.getName()).isNotEmpty();
            assertThat(actualCategory.getImage()).isNotEmpty();
            assertThat(actualCategory.getCreationAt()).containsPattern(PATTERN_OF_CREATE_OR_UPDATE);
            assertThat(actualCategory.getUpdatedAt()).containsPattern(PATTERN_OF_CREATE_OR_UPDATE);
        });

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("GetSingleCategory")
    @Tag("NegativeTest")
    @DisplayName("Get single category with non existed category id")
    public void testGetSingleCategoriesWithNonExistentId() {
/*        int categoryId = 1000;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());

        RecordNotFound responseSingleCategory = categoriesService.getSingleCategory(categoryId, token).getObject("", RecordNotFound.class);

        LocalDateTime date = LocalDateTime.parse(responseSingleCategory.timestamp(), dateTimeFormatter);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(responseSingleCategory.name()).isEqualTo(Constants.NOT_FOUND_ERROR);
            assertThat(responseSingleCategory.message()).startsWith(NOT_FIND_ANY_ENTITY_OF_TYPE);
            assertThat(responseSingleCategory.path()).isEqualTo(PATH + CATEGORY_BASEPATH + "/" + categoryId);
            assertThat(date.getMinute()).isEqualTo(LocalDateTime.now(ZoneOffset.UTC).getMinute());
            assertThat(date.getHour()).isEqualTo(LocalDateTime.now(ZoneOffset.UTC).getHour());
        });*/
    }

    @Test
    @Disabled
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("GetSingleCategory")
    @Tag("NegativeTest")
    @DisplayName("Get single category with category id is not a number")
    public void testGetSingleCategoryWithIdNotNumber() {
/*        String categoryId = "22N";

        JsonPath responseFailed = categoriesService.getSingleCategory(categoryId, 400);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(responseFailed.getString("message")).isEqualTo(NUMERIC_STRING_IS_EXPECTED);
            assertThat(responseFailed.getString("error")).isEqualTo(BAD_REQUEST);
            assertThat(responseFailed.getString("statusCode")).isEqualTo("400");
        });*/
    }

    @Test
    @Tag("API")
    @Tag("CreateCategory")
    @Tag("CategoriesTest")
    @Tag("PositiveTest")
    @DisplayName("Create category")
    @Severity(SeverityLevel.NORMAL)
    void testCreateCategory() {
        String name = "BMW";
        String image = "https://placeimg.com/649/480/any";

        CategoryPOJO response = categoriesService.createCategory(name, image).getObject("", CategoryPOJO.class);

        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getImage()).isEqualTo(image);
    }

    @Test
    @Tag("API")
    @Tag("UpdateCategory")
    @Tag("PositiveTest")
    @DisplayName("Update category")
    @Severity(SeverityLevel.NORMAL)
    void testUpdateCategory() {
        int id = 3;
        String name = "Audi";
        String image = "https://placeimg.com/648/480/any";
        CategoryPOJO response = categoriesService.updateCategory(id, name, image).getObject("", CategoryPOJO.class);

        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getImage()).isEqualTo(image);
    }

    @Test
    @Tag("API")
    @Tag("DeleteCategory")
    @Tag("CategoriesTest")
    @Tag("PositiveTest")
    @DisplayName("Delete category")
    @Severity(SeverityLevel.NORMAL)
    void testDeleteCategory() {

        JsonPath createdCategory = categoriesService.createCategory("Maps", "https://placeimg.com/640/480/any");
        String response = categoriesService.deleteCategory(createdCategory.get("id")).htmlPath().get("html.body");

        assertThat(response).isEqualTo("true");
    }

    @Test
    @Tag("API")
    @Tag("GetAllProductsByCategory")
    @Tag("CategoriesTest")
    @Tag("PositiveTest")
    @DisplayName("Get all products by category")
    @Severity(SeverityLevel.NORMAL)
    void testGetAllProductsByCategory() {
       /* ProductService productService = new ProductService();

        int id = 2;
        List<ProductsPOJO> response = requestCategories.getAllProductsByCategory(id);

        int idInResponseList = response.get(0).getId();
        int idInSingleProduct = productService
                .getSingleProduct(idInResponseList)
                .extractAs(ProductsPOJO.class)
                .getId();

        assertThat(idInResponseList).isEqualTo(idInSingleProduct);*/
    }
}
