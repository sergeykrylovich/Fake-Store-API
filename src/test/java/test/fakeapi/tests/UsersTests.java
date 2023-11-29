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

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API of User")
public class UsersTests {

    RequestUsers requestUsers = new RequestUsers();
    Faker faker = new Faker();


//    @BeforeAll
//    static void setUp() {
//        RestAssured.filters(new AllureRestAssured());
//    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("CreateUser")
    @Tag("Integration")
    @DisplayName("Create new user")
    public void createUserTest() {
        UserPOJO user = requestUsers.createUser();

        UserPOJO singleUser = requestUsers.getSingleUser(user.getId());

        assertThat(user.getId()).isEqualTo(singleUser.getId());

//        System.out.println(String.format("Id = %s, email = %s, name = %s, password = %s, role = %s",
//                user.getId(),
//                user.getEmail(),
//                user.getName(),
//                user.getPassword(),
//                user.getRole()));
    }
    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetAllUser")
    @DisplayName("Get all users")
    public void getAllUsersTest() {
        List<UserPOJO> listOfUsers = requestUsers.getAllUsers();

        System.out.println(listOfUsers.get(0).getId());
    }

    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetSingleUser")
    @DisplayName("Get single user")
    public void getSingleUserTest() {
        UserPOJO user = requestUsers.getSingleUser(1);

        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @DisplayName("Update user by id")
    public void updateUserTest() {

        UserPOJO user = requestUsers.createUser();
        UserPOJO updatedUser = requestUsers.updateUser(user.getId());

        assertThat(user.getId()).isEqualTo(updatedUser.getId());

    }
    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("CheckEmail")
    @Tag("UserTest")
    @DisplayName("Check email")
    public void checkEmailPositiveTest() {

        String email = faker.internet().emailAddress();
        JsonPath check = requestUsers.checkEmail(email);
        boolean result = check.get("isAvailable");

        assertThat(result).isFalse();


    }
}
