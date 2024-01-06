package test.fakeapi.data;

import net.datafaker.Faker;
import test.fakeapi.pojo.CategoryPOJO;

public class CategoryData {

    public static CategoryPOJO getRandomCategory() {
        Faker faker = new Faker();
        return CategoryPOJO.builder()
                .name(faker.name().fullName())
                .image(faker.internet().image())
                .build();
    }
}
