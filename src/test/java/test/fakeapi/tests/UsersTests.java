package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.RequestUsers;

import java.util.List;

@Epic("API of User")
public class UsersTests {

    RequestUsers requestUsers = new RequestUsers();
    Faker faker = new Faker();

    @BeforeAll
    static void setUp() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("CreateUser")
    @DisplayName("Create new user")
    public void createUserTest() {
        UserPOJO user = requestUsers.createUser();

        System.out.println(String.format("Id = %s, email = %s, name = %s, password = %s, role = %s",
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                user.getRole()));
    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("CheckEmail")
    @DisplayName("Check email of user")
    public void getAllUsersTest() {
        List<UserPOJO> listOfUsers = requestUsers.getAllUsers();

        System.out.println(listOfUsers.get(0).getId());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("CheckEmail")
    @DisplayName("Check email of user")
    public void getSingleUserTest() {
        UserPOJO user = requestUsers.getSingleUser(1);

        System.out.println(user.getId());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @DisplayName("Update user by id")
    public void updateUserTest() {
        requestUsers.updateUser(13);


    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("CheckEmail")
    @Tag("UserTest")
    @DisplayName("Check email")
    public void checkEmailPositiveTest() {

        String email = faker.internet().emailAddress();
        JsonPath check = requestUsers.checkEmail(email);
        boolean result = check.get("isAvailable");

        System.out.println(result);
    }
}
