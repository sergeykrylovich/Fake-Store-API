package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.pojo.ProductsPOJO;
import test.fakeapi.pojo.RecordForError;
import test.fakeapi.requests.RequestCategories;
import test.fakeapi.requests.RequestProducts;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.requests.RequestCategories.CATEGORYBASEPATH;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

@Epic("API of Categories")
public class CategoriesTests {

    RequestCategories requestCategories = new RequestCategories();


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("PositiveTest")
    @Tag("GetAllCategories")
    @DisplayName(value = "Get all categories")
    void getAllCategoriesTest() {

        JsonPath allCategories = requestCategories.getAllCategories();

        assertThat(allCategories.getList("", CategoryPOJO.class).size()).isGreaterThan(0);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("GetSingleCategory")
    @Tag("PositiveTest")
    @DisplayName("Get single category")
    public void getSingleCategoriesTest() {

        CategoryPOJO responseSingleCategory = requestCategories.getSingleCategory(1, 200).getObject("", CategoryPOJO.class);

        assertThat(responseSingleCategory.getId()).isEqualTo(1);
        assertThat(responseSingleCategory.getName()).isNotEmpty();
        assertThat(responseSingleCategory.getImage()).isNotEmpty();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("CategoriesTest")
    @Tag("GetSingleCategory")
    @Tag("PositiveTest")
    @DisplayName("Get single category")
    public void getSingleCategoriesWithNonExistentId() {
        int categoryId = 1000;
        RecordForError responseSingleCategory = requestCategories.getSingleCategory(categoryId, 400).getObject("", RecordForError.class);

        assertThat(responseSingleCategory.name()).isEqualTo(NAME);
        assertThat(responseSingleCategory.message()).startsWith(MESSAGE);
        assertThat(responseSingleCategory.path()).isEqualTo(PATH + CATEGORYBASEPATH + "/" + categoryId);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        LocalDateTime date = LocalDateTime.parse(responseSingleCategory.timestamp(), dateTimeFormatter);
        assertThat(date.getMinute()).isEqualTo(LocalDateTime.now(ZoneOffset.UTC).getMinute());
        assertThat(date.getHour()).isEqualTo(LocalDateTime.now(ZoneOffset.UTC).getHour());
        //assertThat(date.getSecond()).isEqualTo(LocalDateTime.now(ZoneOffset.UTC).getSecond());
    }

    @Test
    @Tag("API")
    @Tag("CreateCategory")
    @Tag("CategoriesTest")
    @Tag("Integration")
    @DisplayName("Create category")
    @Severity(SeverityLevel.NORMAL)
    void createCategoryTest() {
        String name = "BMW";
        String image = "https://placeimg.com/649/480/any";

        CategoryPOJO response = requestCategories.createCategory(name, image).getObject("", CategoryPOJO.class);

        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getImage()).isEqualTo(image);
    }

    @Test
    @Tag("API")
    @Tag("UpdateCategory")
    @Tag("Integration")
    @DisplayName("Update category")
    @Severity(SeverityLevel.NORMAL)
    void updateCategoryTest() {
        int id = 3;
        String name = "Audi";
        String image = "https://placeimg.com/648/480/any";
        CategoryPOJO response = requestCategories.updateCategory(id, name, image).getObject("", CategoryPOJO.class);

        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getImage()).isEqualTo(image);
    }

    @Test
    @Tag("API")
    @Tag("DeleteCategory")
    @Tag("CategoriesTest")
    @DisplayName("Delete category")
    @Severity(SeverityLevel.NORMAL)
    void deleteCategoryTest() {

        JsonPath createdCategory = requestCategories.createCategory("Maps", "https://placeimg.com/640/480/any");
        String response = requestCategories.deleteCategory(createdCategory.get("id")).htmlPath().get("html.body");

        assertThat(response).isEqualTo("true");

    }

    @Test
    @Tag("API")
    @Tag("GetAllProductsByCategory")
    @Tag("CategoriesTest")
    @DisplayName("Get all products by category")
    @Severity(SeverityLevel.NORMAL)
    void getAllProductsByCategoryTest() {
        RequestProducts requestProducts = new RequestProducts();

        int id = 2;
        List<ProductsPOJO> response = requestCategories.getAllProductsByCategory(id);

        int idInResponseList = response.get(0).getId();
        int idInSingleProduct = requestProducts
                .getSingleProduct(idInResponseList)
                .getObject("", ProductsPOJO.class)
                .getId();

        assertThat(idInResponseList).isEqualTo(idInSingleProduct);

    }
}
