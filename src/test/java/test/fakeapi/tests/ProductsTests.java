package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.pojo.RecordNotFound;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.AuthenticationRequest;
import test.fakeapi.requests.RequestProducts;
import test.fakeapi.requests.RequestUsers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.requests.RequestProducts.PRODUCTBASEPATH;
import static test.fakeapi.specs.Constants.*;

@Epic("API of products")
public class ProductsTests {

    public static String bearerToken = "";
    Faker faker = new Faker();
    RequestProducts requestProducts = new RequestProducts();


    @BeforeAll
    public static void createAuthToken() {
        RequestUsers requestUsers = new RequestUsers();
        UserPOJO user = requestUsers
                .createUserWithoutArguments()
                .getObject("", UserPOJO.class);;
        bearerToken = AuthenticationRequest.getAccessToken(user.getEmail(), user.getPassword());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Get all products")
    public void getAllProductsTest() {

        ProductsPOJO createdProduct = requestProducts.createProductWithoutArgs(bearerToken);
        List<ProductsPOJO> response = requestProducts.getAllProducts(bearerToken);

        //Checking that creates only 1 record with our product id
        long numberOfResults = response.stream().filter(res -> res.getId().equals(createdProduct.getId())).count();
        assertThat(numberOfResults).isEqualTo(1);

        requestProducts.deleteSingleProduct(createdProduct.getId(), bearerToken, 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Get an existing single product")
    public void getSingleProductTest() {

        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        Integer categoryId = faker.number().numberBetween(1, 5);
        List<String> images = List.of(faker.internet().image());


        //Creating product
        ProductsPOJO createProductItem = requestProducts.createProduct(title,
                price,
                description,
                categoryId,
                images,
                bearerToken);

        JsonPath singleProductResponse = requestProducts.getSingleProduct(createProductItem.getId(), bearerToken, 200);
        assertThat(singleProductResponse.getObject("", ProductsPOJO.class).getId()).isEqualTo(createProductItem.getId());

        requestProducts.deleteSingleProduct(singleProductResponse.getObject("", ProductsPOJO.class).getId(), bearerToken, 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Get a non existing single product")
    public void getSingleProductTestWithNonExistentId() {

        int nonExistingId = requestProducts.getAllProducts(bearerToken).size() + 100;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());

        RecordNotFound singleProductResponse = requestProducts
                .getSingleProduct(nonExistingId, bearerToken, 400)
                .getObject("", RecordNotFound.class);
        LocalDateTime date = LocalDateTime.parse(singleProductResponse.timestamp(), dateTimeFormatter);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(singleProductResponse.name()).isEqualTo(NOT_FOUND_ERROR);
            assertThat(singleProductResponse.message()).startsWith(NOT_FIND_ANY_ENTITY_OF_TYPE);
            assertThat(singleProductResponse.path()).isEqualTo(PATH + PRODUCTBASEPATH + "/" + nonExistingId);
            assertThat(date.getMinute()).isEqualTo(LocalDateTime.now(ZoneOffset.UTC).getMinute());
            assertThat(date.getHour()).isEqualTo(LocalDateTime.now(ZoneOffset.UTC).getHour());
        });


    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Get a non existing single product")
    public void getSingleProductTestWithIdNotANumber() {

        String categoryId = "22N";

        JsonPath responseFailed = requestProducts.getSingleProduct(categoryId, bearerToken, 400);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(responseFailed.getString("message")).isEqualTo(NUMERIC_STRING_IS_EXPECTED);
            assertThat(responseFailed.getString("error")).isEqualTo(BAD_REQUEST);
            assertThat(responseFailed.getString("statusCode")).isEqualTo("400");
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Create product")
    public void createProductTest() {

        //Create fake data for create product API
        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        Integer categoryId = faker.number().numberBetween(1, 5);
        List<String> images = List.of(faker.internet().image());

        //Creating product
        ProductsPOJO createProductItem = requestProducts.createProduct(title,
                price,
                description,
                categoryId,
                images,
                bearerToken);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(createProductItem.getTitle()).isEqualTo(title);
            softly.assertThat(createProductItem.getPrice()).isEqualTo(price);
            softly.assertThat(createProductItem.getDescription()).isEqualTo(description);
            softly.assertThat(createProductItem.getCategory().getId()).isEqualTo(categoryId);
            softly.assertThat(createProductItem.getImages().get(0)).isEqualTo(images.get(0));
        });

        //Delete product after all tests
        requestProducts.deleteSingleProduct(createProductItem.getId(), bearerToken, 200);

    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Update an existing single product")
    public void updateProductTest() {

        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        List<String> images = List.of(faker.internet().image());

        ProductsPOJO createdProduct = requestProducts.createProductWithoutArgs(bearerToken);


        ProductsPOJO updateProductItem = requestProducts.updateProduct(title,
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

        requestProducts.deleteSingleProduct(createdProduct.getId(), bearerToken, 200);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @DisplayName("Delete an existing product")
    public void deleteProductPositiveTest() {

        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        Integer categoryId = faker.number().numberBetween(1, 5);
        List<String> images = List.of(faker.internet().image());

        //Create new product
        Integer productId = requestProducts.createProduct(title,
                price,
                description,
                categoryId,
                images,
                bearerToken).getId();


        //Delete product
        ExtractableResponse<Response> resultOfDelete = requestProducts.deleteSingleProduct(productId, bearerToken, 200);

        assertThat(resultOfDelete.htmlPath().getString("html.body")).isEqualTo("true");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @DisplayName("Delete a non-existing product")
    public void deleteProductNegativeTest() {

        List<ProductsPOJO> listOfProducts = requestProducts.getAllProducts(bearerToken);
        Integer lastId = listOfProducts.get(listOfProducts.size() - 1).getId();

        //Delete product
        ExtractableResponse<Response> resultOfDelete = requestProducts.deleteSingleProduct(lastId + 1000, bearerToken, 400);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(resultOfDelete.jsonPath().getString("name")).isEqualTo(NOT_FOUND_ERROR);
            softly.assertThat(resultOfDelete.jsonPath().getString("message")).startsWith(NOT_FIND_ANY_ENTITY_OF_TYPE);
        });
    }
}
