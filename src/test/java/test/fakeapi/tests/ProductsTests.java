package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.AuthService;
import test.fakeapi.requests.BaseApi;
import test.fakeapi.requests.ProductService;
import test.fakeapi.requests.UserService;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.assertions.Conditions.*;
import static test.fakeapi.requests.ProductService.PRODUCTS_JSON_SCHEMA;
import static test.fakeapi.specs.Constants.NOT_FIND_ANY_ENTITY_OF_TYPE;
import static test.fakeapi.specs.Constants.NUMERIC_STRING_IS_EXPECTED;

@Epic("API of products")
public class ProductsTests extends BaseApi {

    private ProductService productService;
    private AuthService authService;
    private UserService userService;
    private String token;
    private Random random;

    @BeforeEach
    public void initTests() {
        productService = new ProductService();
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
    @Tag("ProductTest")
    @Tag("Integration")
    @Tag("Smoke")
    @DisplayName("Get all products")
    public void getAllProductsTest() {

        List<ProductsPOJO> listOfProducts = productService.getAllProducts(token)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(PRODUCTS_JSON_SCHEMA))
                .should(hasResponseTime(5))
                .asList(ProductsPOJO.class);

        assertThat(listOfProducts).isNotEmpty();

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Smoke")
    @DisplayName("Get an existing single product")
    public void getSingleProductTest() {

        ProductsPOJO expectedProduct = productService.createRandomProduct(token)
                .extractAs(ProductsPOJO.class);

        ProductsPOJO actualProduct = productService.getSingleProduct(expectedProduct.getId(), token)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(PRODUCTS_JSON_SCHEMA))
                .should(hasResponseTime(2))
                .extractAs(ProductsPOJO.class);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualProduct)
                    .usingRecursiveComparison()
                    .ignoringFields("creationAt", "updatedAt", "category")
                    .isEqualTo(expectedProduct);
        });

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Get a non existing single product")
    public void getSingleProductTestWithNonExistentId() {

        random = new Random();

        int maxProductId = productService.getAllProducts().getMaxIdOfProductResponse();

        int nonExistingProductId = maxProductId + random.nextInt(1000, Integer.MAX_VALUE);

        String message = productService
                .getSingleProduct(nonExistingProductId, token)
                .should(hasStatusCode(400))
                .getMessage();

        assertThat(message).startsWith(NOT_FIND_ANY_ENTITY_OF_TYPE);

    }

    @ParameterizedTest
    @ValueSource(strings = "22n")
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Get a non existing single product")
    public void getSingleProductTestWithIdNotANumber(String productId) {

        String message = productService.getSingleProduct(productId, token)
                .should(hasStatusCode(400))
                .getMessage();


        assertThat(message).isEqualTo(NUMERIC_STRING_IS_EXPECTED);

    }

    @MethodSource(value = "test.fakeapi.data.ProductsData#createRandomProduct")
    @ParameterizedTest()
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @Tag("Smoke")
    @DisplayName("Create product")
    public void createProductTest(ProductsPOJO expectedProduct) {

        ProductsPOJO actualProduct = productService.createProduct(expectedProduct, token)
                .should(hasStatusCode(201))
                .should(hasJsonSchema(PRODUCTS_JSON_SCHEMA))
                .should(hasResponseTime(5))
                .extractAs(ProductsPOJO.class);

/*        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualProduct.getId()).isPositive();
            softly.assertThat(actualProduct.getPrice()).isEqualTo(expectedProduct.getPrice());
            //softly.assertThat(actualProduct.getImages()).containsAnyElementsOf(expectedProduct.getImages());
            softly.assertThat(actualProduct.getDescription()).isEqualTo(expectedProduct.getDescription());
            softly.assertThat(actualProduct.getTitle()).isEqualTo(expectedProduct.getTitle());
            softly.assertThat(actualProduct.getCategory()).isNotNull();
            softly.assertThat(actualProduct.getCategory().getId()).isEqualTo(expectedProduct.getCategoryId());
        });*/

        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("categoryId", "id", "creationAt", "updatedAt", "category", "images")
                .isEqualTo(expectedProduct);

        //Delete product after all tests
        productService.deleteSingleProduct(actualProduct.getId(), token);

    }

    @ParameterizedTest
    @MethodSource(value = "test.fakeapi.data.ProductsData#dataForUpdateTest")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Update an existing single product")
    public void updateProductTest(ProductsPOJO actualProduct) {

        ProductsPOJO createdProduct = productService.createRandomProduct(token)
                .extractAs(ProductsPOJO.class);

        ProductsPOJO expectedProduct = productService.updateProduct(createdProduct.getId(), actualProduct, token)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(PRODUCTS_JSON_SCHEMA))
                .extractAs(ProductsPOJO.class);

        assertThat(expectedProduct.getTitle()).isEqualTo(expectedProduct.getTitle());
        assertThat(expectedProduct.getPrice()).isEqualTo(expectedProduct.getPrice());
        assertThat(expectedProduct.getDescription()).isEqualTo(expectedProduct.getDescription());


    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Smoke")
    @DisplayName("Delete an existing product")
    public void deleteExistingProductTest() {

        int createdProductId = productService.createRandomProduct(token)
                .extractAs(ProductsPOJO.class)
                .getId();

        boolean resultOfDelete = productService.deleteSingleProduct(createdProductId, token)
                .should(hasStatusCode(200))
                .getResultOfDelete();

        assertThat(resultOfDelete).isTrue();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @DisplayName("Delete a non-existing product")
    public void deleteNonExistingProduct() {

        random = new Random();

        int maxProductId = productService.getAllProducts().getMaxIdOfProductResponse();
        int nonExistingProductId = maxProductId + random.nextInt(1000, Integer.MAX_VALUE);

        String message = productService.deleteSingleProduct(nonExistingProductId, token)
                .should(hasStatusCode(400))
                .getMessage();

        assertThat(message).startsWith(NOT_FIND_ANY_ENTITY_OF_TYPE);

        productService.deleteSingleProduct(maxProductId, token).should(hasStatusCode(200));

    }
}
