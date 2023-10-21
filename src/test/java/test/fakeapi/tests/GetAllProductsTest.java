package test.fakeapi.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.requests.RequestProducts;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class GetAllProductsTest {
    RequestProducts requestProducts = new RequestProducts();
    @Test
    void getAllProductsWithoutTest() {

        List<ProductsPOJO> response = requestProducts.getAllProducts();
    }

    @Test
    @Tag("Create-test")
     void createProductTest() {
        String title = "String";
        Integer price = 123;
        String description = "AAAAAA";
        Integer categoryId = 1;
        List<String> images= List.of("https://shop.bowandtie.ru/image/cache/data/foto/noski-baboon/Nosk1-v-beluyu-i-krasnuyu-polosku-BAB-S-36-1000x1200.jpg");

        ProductsPOJO createProductItem = requestProducts.createProduct(title,
                1234,
                "only for me!!",
                1,
                images);

        System.out.println(createProductItem);
    }
    @Test
    void updateProductTest() {
        String title = "AAAAAAAAAAA";
        Integer price = 123;
        String description = "AAAAAA";
        List<String> images= List.of("https://shop.bowandtie.ru/image/cache/data/foto/noski-baboon/Noski-v-beluyu-i-krasnuyu-polosku-BAB-S-36-1000x1200.jpg");

        ProductsPOJO createProductItem = requestProducts.updateProduct(title,
                1234,
                "only for me!!",
                images,
                233);

        System.out.println(createProductItem.getTitle());
    }
    @Test
    @Tag("delete")
    void deleteProductTest() {

        Integer productId = requestProducts.createProduct("Title",
                200,
                "HEEELP",
                1,
                List.of("https://shop.bowandtie.ru/image/cache/data/foto/noski-baboon/Nosk1-v-beluyu-i-krasnuyu-polosku-BAB-S-36-1000x1200.jpg")).getId();
        System.out.println("Product id = " + productId);

        String resultOfDelete = requestProducts.deleteSingleProduct(productId);

        assertThat(resultOfDelete).isEqualTo("true");
    }


}
