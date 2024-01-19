package test.fakeapi.data;

import net.datafaker.Faker;
import org.junit.jupiter.params.provider.Arguments;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.requests.CategoriesService;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class CategoryData {

    private CategoriesService categoriesService;

    public static CategoryPOJO getRandomCategory() {
        Faker faker = new Faker();

        CategoryPOJO randomCategory = CategoryPOJO.builder()
                .name(faker.name().fullName())
                .image(faker.internet().image())
                .build();
        return randomCategory;

    }

    public Integer getRandomCategoryId() {

        categoriesService = new CategoriesService();
        List<Integer> categroiesIdList =  categoriesService.getAllCategories()
                .asList(CategoryPOJO.class)
                .stream()
                .map(CategoryPOJO::getId)
                .toList();
        int randomIdOfCategory = new Random().nextInt(categroiesIdList.size() - 1);

        return categroiesIdList.get(randomIdOfCategory);
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
