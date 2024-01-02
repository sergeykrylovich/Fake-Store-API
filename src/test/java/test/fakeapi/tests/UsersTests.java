package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.xml.XmlPath;
import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import test.fakeapi.pojo.InfoMessage;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.BaseApi;
import test.fakeapi.requests.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.assertions.Conditions.*;
import static test.fakeapi.data.RandomUserData.getRandomUser;
import static test.fakeapi.requests.UserService.MESSAGES;
import static test.fakeapi.requests.UserService.USER_JSON_SCHEMA;
import static test.fakeapi.specs.Constants.ADMIN_IS_NOT_FOR_DELETE;

@Epic("API of User")
@DisplayName("User API tests")
public class UsersTests extends BaseApi {

    protected static UserService userService;


    @BeforeEach
    public void initTests() {
        userService = new UserService();
    }



    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetAllUsers")
    @Tag("UserTest")
    @Tag("Smoke")
    @DisplayName("Get all users")
    public void getAllUsersTest() {

        List<UserPOJO> listOfUsers = userService
                .getAllUsers()
                .should(hasStatusCode(200))
                .should(hasJsonSchema(USER_JSON_SCHEMA))
                .should(hasResponseTime(5L))
                .asList(UserPOJO.class);

        assertThat(listOfUsers.size()).isGreaterThan(0);

    }

    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetSingleUser")
    @Tag("UserTest")
    @DisplayName("Get admin user by id")
    public void getAdminUserTest() {

        int userAdminId = 1;
        UserPOJO user = userService
                .getSingleUser(userAdminId)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(USER_JSON_SCHEMA))
                .should(hasResponseTime(5l))
                .extractAs(UserPOJO.class);

        assertThat(user.getId()).isEqualTo(userAdminId);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("CreateUser")
    @Tag("Smoke")
    @Tag("UserTest")
    @DisplayName("Create new user")
    public void createUserTest() {

        UserPOJO user = getRandomUser();

        UserPOJO userApi = userService
                .createUser(user)
                .should(hasStatusCode(201))
                .should(hasJsonSchema(USER_JSON_SCHEMA))
                .should(hasResponseTime(3l))
                .extractAs(UserPOJO.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getName()).isEqualTo(userApi.getName());
            softly.assertThat(user.getPassword()).isEqualTo(userApi.getPassword());
            softly.assertThat(user.getEmail()).isEqualTo(userApi.getEmail());
            softly.assertThat(user.getAvatar()).isEqualTo(userApi.getAvatar());
        });

    }

    @MethodSource(value = "test.fakeapi.data.DataFotTests#dataForUpdateUser")
    @ParameterizedTest()
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @DisplayName("Update user by id")
    public void updateUserTestWithAllArguments(UserPOJO userForUpdate) {

        int userId = userService
                .createRandomUser()
                .extractAs(UserPOJO.class)
                .getId();

        UserPOJO updatedUser = userService
                .updateUser(userId,userForUpdate)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(USER_JSON_SCHEMA))
                .extractAs(UserPOJO.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userId).isEqualTo(updatedUser.getId());
            softly.assertThat(updatedUser.getName()).isEqualTo(userForUpdate.getName());
            softly.assertThat(updatedUser.getAvatar()).isEqualTo(userForUpdate.getAvatar());
            softly.assertThat(updatedUser.getEmail()).isEqualTo(userForUpdate.getEmail());
            softly.assertThat(updatedUser.getRole()).isEqualTo(userForUpdate.getRole());
            softly.assertThat(updatedUser.getPassword()).isEqualTo(userForUpdate.getPassword());
        });

        userService.deleteUser(userId).should(hasStatusCode(200));

    }

    @MethodSource(value = "test.fakeapi.data.DataFotTests#blankDataForUpdateUser")
    @ParameterizedTest()
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @Tag("NegativeTest")
    @DisplayName("Update user by id with blank arguments")
    public void updateUserWithBlankArgumentsTest(UserPOJO updatableUser) {

        UserPOJO user = userService
                .createRandomUser()
                .extractAs(UserPOJO.class);

        List<String> errorUpdatedUser = userService
                .updateUser(user.getId(), updatableUser)
                .should(hasStatusCode(400))
                .getMessageList();

        assertThat(errorUpdatedUser).containsExactlyInAnyOrder(MESSAGES);

        userService.deleteUser(user.getId()).should(hasStatusCode(200));

    }

    @MethodSource(value = "test.fakeapi.data.DataFotTests#dataForUpdateUserWithWrongAvatarAndPassword")
    @ParameterizedTest()
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @Tag("NegativeTest")
    @DisplayName("Update user by id with wrong format of password and avatar")
    public void updateUserTestWithWrongFormatOfPasswordAndAvatar(UserPOJO updatableUser) {

        UserPOJO user = userService
                .createRandomUser()
                .extractAs(UserPOJO.class);

        List<String> listOfMessages = userService
                .updateUser(user.getId(), updatableUser)
                .should(hasStatusCode(400))
                .getMessageList();

        assertThat(listOfMessages).allSatisfy(message -> {
            assertThat(message).containsAnyOf(MESSAGES);
        });

        userService.deleteUser(user.getId()).should(hasStatusCode(200));
    }


    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetSingleUser")
    @Tag("UserTest")
    @Tag("Smoke")
    @DisplayName("Delete single user")
    public void deleteSingleUserTest() {

        int userId = userService.createRandomUser()
                .extractAs(UserPOJO.class)
                .getId();

        XmlPath resultOfDelete = userService
                .deleteUser(userId)
                .should(hasStatusCode(200))
                .asHtmlPath();


        assertThat(resultOfDelete.getBoolean("html.body")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("DeleteUser")
    @Tag("UserTest")
    @Tag("Smoke")
    @DisplayName("Delete admin users")
    public void deleteAdminUsersTest(int userAdminId) {

        InfoMessage user = userService
                .deleteUser(userAdminId)
                .should(hasStatusCode(401))
                .extractAs(InfoMessage.class);

        assertThat(user.getMessage()).isEqualTo(ADMIN_IS_NOT_FOR_DELETE);
    }

    @Test
    @Issue(value = "https://support.mycompany.by/JIRA-1")
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("CheckEmail")
    @Tag("UserTest")
    @DisplayName("Check that email is available")
    public void ExistingEmailTest() {

        Faker faker = new Faker();

        String email = faker.random().nextInt(Integer.MAX_VALUE) + faker.internet().emailAddress();

        boolean isAvailable = userService
                .checkEmailIsAvailable(email)
                .should(hasStatusCode(201))
                .asJsonPath()
                .getBoolean("isAvailable");

        assertThat(isAvailable).isTrue();
    }


    @Test
    @Tag("API")
    @Severity(SeverityLevel.MINOR)
    @Tag("CheckEmail")
    @Tag("UserTest")
    @DisplayName("Check if email is blank")
    public void BlankEmailTest() {

        List<String> messageList = userService
                .checkEmailIsAvailable("")
                .should(hasStatusCode(400))
                .getMessageList();

        assertThat(messageList).contains("email should not be empty").contains("email must be an email");
    }

    @Test
    @Tag("API")
    @Severity(SeverityLevel.MINOR)
    @Tag("CheckEmail")
    @Tag("UserTest")
    @DisplayName("Check if email is not valid")
    public void invalidEmailTest() {

        Faker faker = new Faker();

        String invalidEmail = faker.internet().username();
        List<String> message = userService
                .checkEmailIsAvailable(invalidEmail)
                .should(hasStatusCode(400))
                .getMessageList();

        assertThat(message).contains("email must be an email");

    }
}
