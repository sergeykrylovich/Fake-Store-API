package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import test.fakeapi.listeners.RetryListener;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.*;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.assertions.Conditions.*;
import static test.fakeapi.requests.CategoriesService.CATEGORY_JSON_SCHEMA;
import static test.fakeapi.specs.Constants.*;

@ExtendWith(RetryListener.class)
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
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("Smoke")
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
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("GetSingleCategory")
    @Tag("Smoke")
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
        random = new Random();
        int maxCategoryId = categoriesService.getAllCategories(token).getMaxIdOfCategoryResponse();
        int nonExistingCategoryId = maxCategoryId + random.nextInt(1000, Integer.MAX_VALUE);

        String actualMessage = categoriesService.getSingleCategory(nonExistingCategoryId, token)
                .should(hasStatusCode(400))
                .getMessage();

        assertThat(actualMessage).contains(NOT_FIND_ANY_ENTITY_OF_TYPE);
    }

    @Issue(value = "https://support.mycompany.by/JIRA-2")
    @ParameterizedTest
    @ValueSource(strings = {"22N", "1+1", "3@", "?", "01"})
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("GetSingleCategory")
    @Tag("NegativeTest")
    @DisplayName("Get single category with category id is not a number")
    public void testGetSingleCategoryWithIdNotNumber(String categoryId) {

        String actualMessage = categoriesService.getSingleCategory(categoryId, token)
                .should(hasStatusCode(400))
                .getMessage();

        assertThat(actualMessage).isEqualTo(NUMERIC_STRING_IS_EXPECTED);
    }

    @ParameterizedTest
    @MethodSource(value = "test.fakeapi.data.CategoryData#dataForCreateCategory")
    @Tag("API")
    @Tag("CreateCategory")
    @Tag("CategoriesTest")
    @Tag("Smoke")
    @DisplayName("Create category")
    @Severity(SeverityLevel.CRITICAL)
    void createCategoryTest(CategoryPOJO expectedCategory) {

        CategoryPOJO actualCategory = categoriesService.createCategory(expectedCategory)
                .should(hasStatusCode(201))
                .should(hasJsonSchema(CATEGORY_JSON_SCHEMA))
                .should(hasResponseTime(10))
                .extractAs(CategoryPOJO.class);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(actualCategory.getName()).isEqualTo(expectedCategory.getName());
            assertSoftly.assertThat(actualCategory.getImage()).isEqualTo(expectedCategory.getImage());
            assertSoftly.assertThat(actualCategory.getId()).isPositive();
            assertSoftly.assertThat(actualCategory.getCreationAt()).containsPattern(PATTERN_OF_CREATE_OR_UPDATE);
            assertSoftly.assertThat(actualCategory.getUpdatedAt()).containsPattern(PATTERN_OF_CREATE_OR_UPDATE);
        });
    }

    @ParameterizedTest
    @MethodSource(value = "test.fakeapi.data.CategoryData#dataForCreateCategory")
    @Tag("API")
    @Tag("UpdateCategory")
    @Tag("PositiveTest")
    @DisplayName("Update category")
    @Severity(SeverityLevel.CRITICAL)
    void updateCategoryTest(CategoryPOJO category) {

        CategoryPOJO expectedCategory = categoriesService.createCategory(category).extractAs(CategoryPOJO.class);

        CategoryPOJO actualCategory = categoriesService.updateCategory(expectedCategory.getId(), category, token)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(CATEGORY_JSON_SCHEMA))
                .extractAs(CategoryPOJO.class);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(actualCategory.getName())
                    .as("name of category")
                    .isEqualTo(expectedCategory.getName());
            assertSoftly.assertThat(actualCategory.getImage()).isEqualTo(expectedCategory.getImage());
            assertSoftly.assertThat(actualCategory.getId()).isPositive();
            assertSoftly.assertThat(actualCategory.getCreationAt()).containsPattern(PATTERN_OF_CREATE_OR_UPDATE);
            assertSoftly.assertThat(actualCategory.getUpdatedAt()).containsPattern(PATTERN_OF_CREATE_OR_UPDATE);
        });
    }

    @Test
    @Tag("API")
    @Tag("DeleteCategory")
    @Tag("CategoriesTest")
    @Tag("Smoke")
    @DisplayName("Delete category")
    @Severity(SeverityLevel.NORMAL)
    void deleteCategoryTest() {

        CategoryPOJO createdCategory = categoriesService.createRandomCategory(token);
        boolean resultOfDelete = categoriesService.deleteCategory(createdCategory.getId(), token)
                .should(hasStatusCode(200))
                .getResultOfDelete();

        assertThat(resultOfDelete).isTrue();
    }

    @Test
    @Tag("API")
    @Tag("GetAllProductsByCategory")
    @Tag("CategoriesTest")
    @Tag("Smoke")
    @DisplayName("Get all products by category")
    @Severity(SeverityLevel.NORMAL)
    void getAllProductsByCategoryTest() {

        ProductService productService = new ProductService();

        Integer categoryId = productService.createRandomProduct(token)
                .extractAs(ProductsPOJO.class)
                .getCategory()
                .getId();

        List<ProductsPOJO> actualListOfProducts = categoriesService.getAllProductsByCategory(categoryId, token)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(ProductService.PRODUCTS_JSON_SCHEMA))
                .asList(ProductsPOJO.class);

        assertThat(actualListOfProducts).isNotNull();
        boolean hasCategoryId = actualListOfProducts.stream()
                .map(x -> x.getCategory().getId())
                .filter(x -> x == categoryId)
                .findAny()
                .isPresent();
        assertThat(hasCategoryId).isTrue();

    }
}
