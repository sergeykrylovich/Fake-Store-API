package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.requests.AuthenticationRequest;
import test.fakeapi.requests.RequestProducts;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API of products")
public class ProductsTests {

    Faker faker = new Faker();
    RequestProducts requestProducts = new RequestProducts();
    @Test
    @Tag("ProductTest")
    @DisplayName("Get all products")
    void getAllProductsTest() {

        String bearerToken = AuthenticationRequest.getAccessToken();

        List<ProductsPOJO> responseBefore = requestProducts.getAllProducts(bearerToken);
        System.out.println("Size of list before = " + responseBefore.size());

        ProductsPOJO createdProduct = requestProducts.createProductWithoutArgs(bearerToken);
        System.out.println("ProductId = " + createdProduct.getId());

        List<ProductsPOJO> responseAfter = requestProducts.getAllProducts(bearerToken);
        System.out.println("Size of list after = " + responseAfter.size());


        assertThat(responseAfter.size()).isEqualTo(responseBefore.size() + 1);

        //Checking that creates only 1 record with our product id
        long numberOfResults = responseAfter.stream().filter(afterList -> afterList.getId().equals(createdProduct.getId())).count();
        assertThat(numberOfResults).isEqualTo(1);

        requestProducts.deleteSingleProduct(createdProduct.getId(), bearerToken, 200);

    }

    @Test
    @Tag("ProductTest")
    @DisplayName("Get an existing single product")
    void getSingleProductTest() {

        String bearerToken = AuthenticationRequest.getAccessToken();

        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        Integer categoryId = faker.number().numberBetween(1, 5);
        List<String> images= List.of(faker.internet().image());


        //Creating product
        ProductsPOJO createProductItem = requestProducts.createProduct(title,
                price,
                description,
                categoryId,
                images,
                bearerToken);

        ProductsPOJO singleProductResponse = requestProducts.getSingleProduct(createProductItem.getId(), bearerToken);
        System.out.println(singleProductResponse.getId());
        assertThat(singleProductResponse.getId()).isEqualTo(createProductItem.getId());

        requestProducts.deleteSingleProduct(singleProductResponse.getId(), bearerToken, 200);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("ProductTest")
    @DisplayName("Create product")
     void createProductTest() {

        RestAssured.filters(new AllureRestAssured());
        //Get access token
        String bearerToken = AuthenticationRequest.getAccessToken();


        //Create fake data for create product API
        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        Integer categoryId = faker.number().numberBetween(1, 5);
        List<String> images= List.of(faker.internet().image());

        //Creating product
        ProductsPOJO createProductItem = requestProducts.createProduct(title,
                price,
                description,
                categoryId,
                images,
                bearerToken);

        assertThat(createProductItem.getTitle()).isEqualTo(title);
        assertThat(createProductItem.getPrice()).isEqualTo(price);
        assertThat(createProductItem.getDescription()).isEqualTo(description);
        assertThat(createProductItem.getCategory().getId()).isEqualTo(categoryId);
        assertThat(createProductItem.getImages().get(0)).isEqualTo(images.get(0));

        //Delete product after all tests
        String resultOfDelete = requestProducts.deleteSingleProduct(createProductItem.getId(), bearerToken, 200);

        assertThat(resultOfDelete).isEqualTo("true");
    }


    @Test
    @Tag("ProductTest")
    @DisplayName("Update an existing single product")
    void updateProductTest() {

        String bearerToken = AuthenticationRequest.getAccessToken();

        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        List<String> images= List.of(faker.internet().image());

        List<ProductsPOJO> listOfProducts =  requestProducts.getAllProducts(bearerToken);
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
    @Tag("ProductTest")
    @DisplayName("Delete an existing product")
    void deleteProductPositiveTest() {

        //Get access token
        String bearerToken = AuthenticationRequest.getAccessToken();

        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        Integer categoryId = faker.number().numberBetween(1, 5);
        List<String> images= List.of(faker.internet().image());

        //Create new product
        Integer productId = requestProducts.createProduct(title,
                price,
                description,
                categoryId,
                images,
                bearerToken).getId();

        System.out.println("Product id = " + productId);

        //Delete product
        String resultOfDelete = requestProducts.deleteSingleProduct(productId, bearerToken, 200);

        assertThat(resultOfDelete).isEqualTo("true");
    }
    @Test
    @Tag("ProductTest")
    @DisplayName("Delete a non-existing product")
    void deleteProductNegativeTest() {

        //Get access token
        String bearerToken = AuthenticationRequest.getAccessToken();

        List<ProductsPOJO> listOfProducts =  requestProducts.getAllProducts(bearerToken);
        Integer lastId = listOfProducts.get(listOfProducts.size() - 1).getId();
        //Create new product


        System.out.println("Product id = " + lastId);

        //Delete product
        String resultOfDelete = requestProducts.deleteSingleProduct(lastId + 1 , bearerToken, 400);

        //assertThat(resultOfDelete).isEqualTo("true");
    }
}
