package test.fakeapi.data;

import net.datafaker.Faker;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.requests.CategoriesService;

import java.util.List;
import java.util.Random;

public class CategoryData {

    private CategoriesService categoriesService;

    public CategoryPOJO createRandomCategory() {
        Faker faker = new Faker();
        categoriesService = new CategoriesService();
        CategoryPOJO singleCategory = CategoryPOJO.builder()
                .name(faker.name().fullName())
                .image(faker.internet().image())
                .build();
        return categoriesService.createCategory(singleCategory).extractAs(CategoryPOJO.class);

    }

    public Integer getRandomCategory() {

        categoriesService = new CategoriesService();
        List<Integer> categroiesIdList =  categoriesService.getAllCategories()
                .asList(CategoryPOJO.class)
                .stream()
                .map(CategoryPOJO::getId)
                .toList();
        int randomIdOfCategory = new Random().nextInt(categroiesIdList.size() - 1);

        return categroiesIdList.get(randomIdOfCategory);

    }
}
