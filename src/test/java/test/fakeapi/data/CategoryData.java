package test.fakeapi.data;

import net.datafaker.Faker;
import org.junit.jupiter.params.provider.Arguments;
import test.fakeapi.pojo.CategoryPOJO;

import java.util.stream.Stream;

public class CategoryData {


    public static CategoryPOJO getRandomCategory() {
        Faker faker = new Faker();

        CategoryPOJO randomCategory = CategoryPOJO.builder()
                .name(faker.name().fullName())
                .image(faker.internet().image())
                .build();
        return randomCategory;

    }

    public static Stream<Arguments> dataForCreateCategory() {
        Faker faker = new Faker();

        CategoryPOJO randomCategory = CategoryPOJO.builder()
                .name(faker.name().fullName())
                .image(faker.internet().image())
                .build();

        return Stream.of(Arguments.of(randomCategory));
    }
}
