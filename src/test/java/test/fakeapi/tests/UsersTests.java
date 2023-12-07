package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import jdk.security.jarsigner.JarSigner;
import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.RequestUsers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API of User")
public class UsersTests {

    RequestUsers requestUsers = new RequestUsers();
    Faker faker = new Faker();


    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetAllUser")
    @DisplayName("Get all users")
    public void getAllUsersTest() {
        List<UserPOJO> listOfUsers = requestUsers
                .getAllUsers()
                .getList("", UserPOJO.class);

        assertThat(listOfUsers.size()).isGreaterThan(0);
    }

    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetSingleUser")
    @DisplayName("Get single user")
    public void getSingleUserTest() {
        int userId = 1;
        UserPOJO user = requestUsers
                .getSingleUser(userId)
                .getObject("", UserPOJO.class);

        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("CreateUser")
    @Tag("Integration")
    @DisplayName("Create new user")
    public void createUserTest() {
        UserPOJO createdUser = requestUsers
                .createUserWithoutArguments()
                .getObject("", UserPOJO.class);

        UserPOJO singleUser = requestUsers
                .getSingleUser(createdUser.getId())
                .getObject("", UserPOJO.class);

        assertThat(createdUser.getId()).isEqualTo(singleUser.getId());
        assertThat(createdUser.getName()).isEqualTo(singleUser.getName());
        assertThat(createdUser.getRole()).isEqualTo(singleUser.getRole());
        assertThat(createdUser.getAvatar()).isEqualTo(singleUser.getAvatar());
        assertThat(createdUser.getPassword()).isEqualTo(singleUser.getPassword());

    }

    @MethodSource(value = "test.fakeapi.data.DataFotTests#dataForUpdateUser")
    @ParameterizedTest()
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @DisplayName("Update user by id")
    public void updateUserTestWithAllArguments(String name, String email, String password, String avatar, String role) {

        UserPOJO user = requestUsers
                .createUserWithoutArguments()
                .getObject("", UserPOJO.class);

        UserPOJO updatedUser = requestUsers
                .updateUser(user.getId(),200, name, email, password, avatar, role)
                .getObject("", UserPOJO.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getId()).isEqualTo(updatedUser.getId());
            softly.assertThat(updatedUser.getName()).isEqualTo(name);
            softly.assertThat(updatedUser.getAvatar()).isEqualTo(avatar);
            softly.assertThat(updatedUser.getEmail()).isEqualTo(email);
            softly.assertThat(updatedUser.getRole()).isEqualTo(role);
            softly.assertThat(updatedUser.getPassword()).isEqualTo(password);
        });

    }
    @MethodSource(value = "test.fakeapi.data.DataFotTests#dataForUpdateUserNegative")
    @ParameterizedTest()
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @Tag("NegativeTest")
    @DisplayName("Update user by id without role")
    public void updateUserTestWithoutRole(String name, String email, String password, String avatar, String role) {

        UserPOJO user = requestUsers
                .createUserWithoutArguments()
                .getObject("", UserPOJO.class);
        JsonPath errorUpdatedUser = requestUsers
                .updateUser(user.getId(), 400, name, email, password, avatar, role);

       assertThat(errorUpdatedUser.getList("message").get(0)).isEqualTo("role must be one of the following values: admin, customer");

    }

    @Test
    @Issue(value = "https://support.mycompany.by/JIRA-1")
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("CheckEmail")
    @Tag("UserTest")
    @DisplayName("Check that email is available")
    public void checkEmailPositiveTest() {

        String email = faker.internet().emailAddress();
        boolean result = requestUsers
                .checkEmail(email)
                .get("isAvailable");

        assertThat(result).isTrue();


    }
}
