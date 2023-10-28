package test.fakeapi.tests;

import net.datafaker.Faker;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.requests.AuthenticationRequest;
import test.fakeapi.requests.RequestProducts;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductsTests {
    RequestProducts requestProducts = new RequestProducts();
    @Test
    @Tag("ProductTest")
    void getAllProductsWithoutTest() {

        String bearerToken = AuthenticationRequest.getAccessToken();

        List<ProductsPOJO> response = requestProducts.getAllProducts(bearerToken);
    }
    @Test
    @Tag("ProductTest")
    void getSingleProductTest() {

        String bearerToken = AuthenticationRequest.getAccessToken();

        ProductsPOJO response = requestProducts.getSingleProduct(253, bearerToken);
        assertThat(response.getId()).isEqualTo(253);
    }

    @Test
    @Tag("ProductTest")
     void createProductTest() {

        String bearerToken = AuthenticationRequest.getAccessToken();

        String title = "String";
        Integer price = 123;
        String description = "AAAAAA";
        Integer categoryId = 1;
        List<String> images= List.of("https://shop.bowandtie.ru/image/cache/data/foto/noski-baboon/Nosk1-v-beluyu-i-krasnuyu-polosku-BAB-S-36-1000x1200.jpg");

        ProductsPOJO createProductItem = requestProducts.createProduct(title,
                1234,
                "only for me!!",
                1,
                images,
                bearerToken);

        System.out.println(createProductItem);
    }

    @Test
    @Tag("ProductTest")
     void createAuthTest() {

        String bearerToken = AuthenticationRequest.getAccessToken();

        String title = "String1";
        Integer price = 124;
        String description = "AAAAAAs";
        Integer categoryId = 1;
        List<String> images= List.of("https://shop.bowandtie.ru/image/cache/data/foto/noski-baboon/Nosk1-v-beluyu-i-krasnuyu-polosku-BAB-S-36-1000x1200.jpg");

        ProductsPOJO createProductItem = requestProducts.createProduct(title,
                1234,
                "only for me!!",
                1,
                images,
                bearerToken);

        System.out.println(createProductItem);
    }

    @Test
    @Tag("ProductTest")
    void updateProductTest() {
        Faker faker = new Faker();
        String bearerToken = AuthenticationRequest.getAccessToken();

        String title = faker.brand().watch();
        Integer price = faker.number().numberBetween(0, 1000);
        String description = faker.text().text(10, 100);
        List<String> images= List.of(faker.internet().image());
        //"https://shop.bowandtie.ru/image/cache/data/foto/noski-baboon/Noski-v-beluyu-i-krasnuyu-polosku-BAB-S-36-1000x1200.jpg"

        ProductsPOJO createProductItem = requestProducts.updateProduct(title,
                price,
                description,
                images,
                251,
                bearerToken);

        assertThat(createProductItem.getTitle()).isEqualTo(title);
        assertThat(createProductItem.getPrice()).isEqualTo(price);
        assertThat(createProductItem.getDescription()).isEqualTo(description);
        assertThat(createProductItem.getImages().get(0)).isEqualTo(images.get(0));
    }

    @Test
    @Tag("ProductTest")
    void deleteProductTest() {

        //Get access token
        String bearerToken = AuthenticationRequest.getAccessToken();

        //Create new product
        Integer productId = requestProducts.createProduct("Title",
                200,
                "HEEELP",
                1,
                List.of("https://shop.bowandtie.ru/image/cache/data/foto/noski-baboon/Nosk1-v-beluyu-i-krasnuyu-polosku-BAB-S-36-1000x1200.jpg"),
                bearerToken).getId();
        System.out.println("Product id = " + productId);

        //Delete product
        String resultOfDelete = requestProducts.deleteSingleProduct(productId, bearerToken);

        assertThat(resultOfDelete).isEqualTo("true");


    }


}
