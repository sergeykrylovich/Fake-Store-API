package test.fakeapi.tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.requests.RequestCategories;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.requests.RequestCategories.*;

public class CategoriesTests {

    RequestCategories requestCategories = new RequestCategories();

    @Test
    void getAllCategoriesTest() {
        getAllCategories();
    }

    @Test
    void getSingleCategoriesTest() {
        CategoryPOJO responseSingleCategory = getSingleCategory(1).getObject("", CategoryPOJO.class);

        assertThat(responseSingleCategory.getId()).isEqualTo(1);
    }

    @Test
    @Tag("CreateCategory")
    @Tag("CategoriesTest")
    @Tag("Integration")
    @DisplayName("Create category")
    @Severity(SeverityLevel.NORMAL)
    void createCategoryTest() {
        String name = "BMW";
        String image = "https://placeimg.com/649/480/any";

        CategoryPOJO response= createCategory(name, image).getObject("", CategoryPOJO.class);

        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getImage()).isEqualTo(image);
    }

    @Test
    @Tag("UpdateCategory")
    @Tag("CategoriesTest")
    @Tag("Integration")
    @DisplayName("Update category")
    @Severity(SeverityLevel.NORMAL)
    void updateCategoryTest() {
        int id = 3;
        String name = "Audi";
        String image = "https://placeimg.com/648/480/any";
        CategoryPOJO response= updateCategory(id, name, image).getObject("", CategoryPOJO.class);

        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getImage()).isEqualTo(image);
    }

    @Test
    @Tag("DeleteCategory")
    @Tag("CategoriesTest")
    @DisplayName("Delete category")
    @Severity(SeverityLevel.NORMAL)
    void deleteCategoryTest() {
        int id = 17;
        String response= deleteCategory(id).htmlPath().get("html.body");

        assertThat(response).isEqualTo("true");

    }

    @Test
    @Tag("GetAllProductsByCategory")
    @Tag("CategoriesTest")
    @DisplayName("Get all products by category")
    @Severity(SeverityLevel.NORMAL)
    void getAllProductsByCategoryTest() {
        int id = 1;
        List<ProductsPOJO> response = getAllProductsByCategory(id);

        assertThat(response.get(0).getId()).isEqualTo(5);

    }
}
