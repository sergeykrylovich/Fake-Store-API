package test.fakeapi.data;

import net.datafaker.Faker;
import test.fakeapi.pojo.ProductsPOJO;

import java.util.List;

public class ProductsData {

    public static ProductsPOJO getRandomProduct() {
        Faker faker = new Faker();
        return ProductsPOJO.builder()
                .title(faker.brand().watch())
                .price(faker.number().numberBetween(0, 1000))
                .description(faker.text().text(10, 100))
                .categoryId(faker.number().numberBetween(1, 5))
                .images(List.of(faker.internet().image()))
                .build();

    }
}
