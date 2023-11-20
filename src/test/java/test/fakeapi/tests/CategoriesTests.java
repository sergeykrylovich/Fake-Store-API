package test.fakeapi.tests;

import org.junit.jupiter.api.Test;
import test.fakeapi.requests.RequestCategories;

public class CategoriesTests {

    RequestCategories requestCategories = new RequestCategories();

    @Test
    void getAllCategoriesTest() {
        requestCategories.getAllCategories();
    }
}
