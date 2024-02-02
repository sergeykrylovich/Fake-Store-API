package test.fakeapi.data;

import net.datafaker.Faker;
import org.junit.jupiter.params.provider.Arguments;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.requests.CategoriesService;

import java.util.List;
import java.util.stream.Stream;

public class ProductsData {

    public static ProductsPOJO getRandomProduct() {
        Faker faker = new Faker();
        return ProductsPOJO.builder()
                .title(faker.brand().watch())
                .price(faker.number().numberBetween(0, 1000))
                .description(faker.text().text(10, 100))
                .categoryId(1)
                .images(List.of(faker.internet().image()))
                .build();

    }

    public static Stream<Arguments> createRandomProduct() {
        Faker faker = new Faker();
        CategoriesService categoriesService = new CategoriesService();

        ProductsPOJO product = ProductsPOJO.builder()
                .title(faker.brand().watch())
                .price(faker.number().numberBetween(0, 1000))
                .description(faker.text().text(10, 100))
                .categoryId(categoriesService.getRandomCategoryId())
                .images(List.of(faker.internet().image()))
                .build();

        return Stream.of(Arguments.of(product));
    }

    public static Stream<Arguments> dataForUpdateTest() {
        Faker faker = new Faker();
        ProductsPOJO product = ProductsPOJO.builder()
                .title(faker.brand().watch())
                .price(faker.number().numberBetween(0, 1000))
                .description(faker.text().text(10, 100))
                .images(List.of(faker.internet().image()))
                .build();

        return Stream.of(Arguments.of(product));
    }

}
