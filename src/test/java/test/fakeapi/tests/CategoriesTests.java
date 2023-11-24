package test.fakeapi.tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.requests.RequestCategories;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.requests.RequestCategories.*;

public class CategoriesTests {

    RequestCategories requestCategories = new RequestCategories();

    @Test
    void getAllCategoriesTest() {
        requestCategories.getAllCategories();
    }

    @Test
    @Tag("CreateCategory")
    @Tag("CategoriesTest")
    @DisplayName("Create category")
    @Severity(SeverityLevel.NORMAL)
    void createCategoryTest() {
        CategoryPOJO response= createCategory().getObject("", CategoryPOJO.class);

        assertThat(response.getName()).isEqualTo("Plane");
        assertThat(response.getImage()).isEqualTo("https://placeimg.com/640/480/any");
    }

    @Test
    @Tag("CreateCategory")
    @Tag("CategoriesTest")
    @DisplayName("Create category")
    @Severity(SeverityLevel.NORMAL)
    void updateCategoryTest() {
        CategoryPOJO response= createCategory().getObject("", CategoryPOJO.class);

        assertThat(response.getName()).isEqualTo("Plane");
        assertThat(response.getImage()).isEqualTo("https://placeimg.com/640/480/any");
    }
}
