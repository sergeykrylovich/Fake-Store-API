package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.AuthenticationRequest;
import test.fakeapi.requests.RequestProducts;
import test.fakeapi.requests.RequestUsers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API of products")
public class ProductsTests {



    public static String bearerToken = "";
    Faker faker = new Faker();
    RequestProducts requestProducts = new RequestProducts();

    @BeforeAll
    public void createAuthToken() {
        RequestUsers requestUsers = new RequestUsers();
        UserPOJO user = requestUsers.createUser();
        bearerToken = AuthenticationRequest.getAccessToken(user.getEmail(), user.getPassword());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Get all products")
    void getAllProductsTest() {


        System.out.println(bearerToken + " - 1");


        ProductsPOJO createdProduct = requestProducts.createProductWithoutArgs(bearerToken);
        List<ProductsPOJO> response = requestProducts.getAllProducts(bearerToken);

        //Checking that creates only 1 record with our product id
        long numberOfResults = response.stream().filter(res -> res.getId().equals(createdProduct.getId())).count();
        assertThat(numberOfResults).isEqualTo(1);

        System.out.println(bearerToken + "2");
        requestProducts.deleteSingleProduct(createdProduct.getId(), bearerToken, 200);


    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Get an existing single product")
    void getSingleProductTest() throws InterruptedException {

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

        ProductsPOJO singleProductResponse = requestProducts.getSingleProduct(createProductItem.getId(), bearerToken);
        //System.out.println(singleProductResponse.getId());
        assertThat(singleProductResponse.getId()).isEqualTo(createProductItem.getId());
        //Thread.sleep(100);
        requestProducts.deleteSingleProduct(singleProductResponse.getId(), bearerToken, 200);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Create product")
    void createProductTest() {

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

        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(createProductItem.getTitle()).isEqualTo(title);
        softAssert.assertThat(createProductItem.getPrice()).isEqualTo(price);
        softAssert.assertThat(createProductItem.getDescription()).isEqualTo(description);
        softAssert.assertThat(createProductItem.getCategory().getId()).isEqualTo(categoryId);
        softAssert.assertThat(createProductItem.getImages().get(0)).isEqualTo(images.get(0));

        //Delete product after all tests
        String resultOfDelete = requestProducts.deleteSingleProduct(createProductItem.getId(), bearerToken, 200);

        assertThat(resultOfDelete).isEqualTo("true");
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @Tag("Integration")
    @DisplayName("Update an existing single product")
    void updateProductTest() {

        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        List<String> images = List.of(faker.internet().image());

        List<ProductsPOJO> listOfProducts = requestProducts.getAllProducts(bearerToken);
        Integer lastProductId = listOfProducts.get(listOfProducts.size() - 1).getId();

        ProductsPOJO createProductItem = requestProducts.updateProduct(title,
                price,
                description,
                images,
                lastProductId,
                bearerToken);

        assertThat(createProductItem.getTitle()).isEqualTo(title);
        assertThat(createProductItem.getPrice()).isEqualTo(price);
        assertThat(createProductItem.getDescription()).isEqualTo(description);
        assertThat(createProductItem.getImages().get(0)).isEqualTo(images.get(0));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @DisplayName("Delete an existing product")
    void deleteProductPositiveTest() {

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
        String resultOfDelete = requestProducts.deleteSingleProduct(productId, bearerToken, 200);

        assertThat(resultOfDelete).isEqualTo("true");
    }

    @Test
    @Disabled
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("ProductTest")
    @DisplayName("Delete a non-existing product")
    void deleteProductNegativeTest() {

        List<ProductsPOJO> listOfProducts = requestProducts.getAllProducts(bearerToken);
        Integer lastId = listOfProducts.get(listOfProducts.size() - 1).getId();
        //Create new product

        //Delete product
        String resultOfDelete = requestProducts.deleteSingleProduct(lastId + 1, bearerToken, 400);

       // assertThat(resultOfDelete).isEqualTo("true");
    }
}
