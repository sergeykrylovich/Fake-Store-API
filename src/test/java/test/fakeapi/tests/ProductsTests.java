package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import test.fakeapi.pojo.ProductsPOJO;
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

    Faker faker = new Faker();
    private ProductService productService;
    private AuthService authService;
    private UserService userService;

    @BeforeEach
    public void initTests() {
        productService = new ProductService();
        authService = new AuthService();
        userService = new UserService();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @Tag("Smoke")
    @DisplayName("Get all products")
    public void getAllProductsTest() {

        String accessToken = authService.createAndLoginRandomUser().getJWTToken();

        List<ProductsPOJO> listOfProducts = productService.getAllProducts(accessToken)
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
    @Tag("Integration")
    @DisplayName("Get an existing single product")
    public void getSingleProductTest() {

        String accessToken = authService.createAndLoginRandomUser().getJWTToken();
        ProductsPOJO createdProduct = productService.createRandomProduct(accessToken)
                .extractAs(ProductsPOJO.class);

        ProductsPOJO responseProduct = productService.getSingleProduct(createdProduct.getId(), accessToken)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(PRODUCTS_JSON_SCHEMA))
                .should(hasResponseTime(2))
                .extractAs(ProductsPOJO.class);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(responseProduct.getId()).isEqualTo(createdProduct.getId());
            softAssertions.assertThat(responseProduct.getTitle()).isEqualTo(createdProduct.getTitle());
            softAssertions.assertThat(responseProduct.getDescription()).isEqualTo(createdProduct.getDescription());
            softAssertions.assertThat(responseProduct.getCategoryId()).isEqualTo(createdProduct.getCategoryId());
            softAssertions.assertThat(responseProduct.getImages()).isEqualTo(createdProduct.getImages());
            softAssertions.assertThat(responseProduct.getPrice()).isEqualTo(createdProduct.getPrice());
        });

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Get a non existing single product")
    public void getSingleProductTestWithNonExistentId() {

        Random random = new Random();

        String accessToken = authService.createAndLoginRandomUser().getJWTToken();
        int createdProductId = productService.createRandomProduct(accessToken)
                .extractAs(ProductsPOJO.class)
                .getId();

        int nonExistingId = createdProductId + random.nextInt(1000, Integer.MAX_VALUE);

        String message = productService
                .getSingleProduct(nonExistingId, accessToken)
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

        String accessToken = authService.createAndLoginRandomUser().getJWTToken();

        String message = productService.getSingleProduct(productId, accessToken)
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
    @DisplayName("Create product")
    public void createProductTest(ProductsPOJO product) {

        String accessToken = authService.createAndLoginRandomUser().getJWTToken();
        ProductsPOJO createdProduct = productService.createProduct(product, accessToken)
                .should(hasStatusCode(201))
                .should(hasJsonSchema(PRODUCTS_JSON_SCHEMA))
                .should(hasResponseTime(5))
                .extractAs(ProductsPOJO.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(createdProduct.getId()).isPositive();
            softly.assertThat(createdProduct.getPrice()).isEqualTo(product.getPrice());
            softly.assertThat(createdProduct.getImages()).containsAnyElementsOf(product.getImages());
            softly.assertThat(createdProduct.getDescription()).isEqualTo(product.getDescription());
            softly.assertThat(createdProduct.getTitle()).isEqualTo(product.getTitle());
            softly.assertThat(createdProduct.getCategory()).isNotNull();
//            softly.assertThat(createdProduct.getCategory().getImage()).isEqualTo(product.getCategory().getImage());
//            softly.assertThat(createdProduct.getCategory().getName()).isEqualTo(product.getCategory().getName());
        });

        //Delete product after all tests
        productService.deleteSingleProduct(createdProduct.getId(), accessToken);

    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Update an existing single product")
    public void updateProductTest() {

/*
        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        List<String> images = List.of(faker.internet().image());

        ProductsPOJO createdProduct = productService.createRandomProduct(bearerToken);


        ProductsPOJO updateProductItem = productService.updateProduct(title,
                price,
                description,
                images,
                createdProduct.getId(),
                bearerToken);


        SoftAssertions.assertSoftly(softly -> {
            assertThat(updateProductItem.getTitle()).isEqualTo(title);
            assertThat(updateProductItem.getPrice()).isEqualTo(price);
            assertThat(updateProductItem.getDescription()).isEqualTo(description);
            assertThat(updateProductItem.getImages().get(0)).isEqualTo(images.get(0));
        });
*/


    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Smoke")
    @DisplayName("Delete an existing product")
    public void deleteExistingProductTest() {

        String accessToken = authService.createAndLoginRandomUser().getJWTToken();
        ProductsPOJO createdProduct = productService.createRandomProduct(accessToken)
                .extractAs(ProductsPOJO.class);

        boolean resultOfDelete = productService.deleteSingleProduct(createdProduct.getId(), accessToken)
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

        Random random = new Random();

        String accessToken = authService.createAndLoginRandomUser().getJWTToken();
        int productId = productService.createRandomProduct(accessToken)
                .should(hasStatusCode(201))
                .extractAs(ProductsPOJO.class)
                .getId();

        int nonExistingProductId = productId + random.nextInt(1000, Integer.MAX_VALUE);

        String message = productService.deleteSingleProduct(nonExistingProductId, accessToken)
                .should(hasStatusCode(400))
                .getMessage();

        assertThat(message).startsWith(NOT_FIND_ANY_ENTITY_OF_TYPE);

        productService.deleteSingleProduct(productId, accessToken).should(hasStatusCode(200));

    }
}
