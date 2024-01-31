package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import test.fakeapi.listeners.SaveFailedTests;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.AuthService;
import test.fakeapi.requests.BaseApi;
import test.fakeapi.requests.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.assertions.Conditions.*;
import static test.fakeapi.data.UserData.getRandomUser;
import static test.fakeapi.specs.Constants.MESSAGES;
import static test.fakeapi.requests.UserService.USER_JSON_SCHEMA;
import static test.fakeapi.specs.Constants.ADMIN_IS_NOT_FOR_DELETE;

@ExtendWith(SaveFailedTests.class)
@Epic("API of User")
@DisplayName("User API tests")
public class UsersTests extends BaseApi {

    private UserService userService;
    private AuthService authService;


    @BeforeEach
    public void initTests() {
        userService = new UserService();
        authService = new AuthService();
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

        assertThat(listOfUsers).isNotEmpty();
    }

    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetSingleUser")
    @Tag("UserTest")
    @DisplayName("Get admin user by id")
    public void getAdminUserTest() {

        int userAdminId = 1;
        String token = authService.logInAdminUser().getJWTToken();
        UserPOJO user = userService
                .getSingleUser(userAdminId, token)
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

        UserPOJO expectedUser = getRandomUser();

        UserPOJO actualUser = userService
                .createUser(expectedUser)
                .should(hasStatusCode(201))
                .should(hasJsonSchema(USER_JSON_SCHEMA))
                .should(hasResponseTime(3l))
                .extractAs(UserPOJO.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
            softly.assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
            softly.assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
            softly.assertThat(actualUser.getAvatar()).isEqualTo(expectedUser.getAvatar());
        });

        userService.deleteUser(actualUser.getId()).should(hasStatusCode(200));

    }

    @MethodSource(value = "test.fakeapi.data.DataFotTests#dataForUpdateUser")
    @ParameterizedTest()
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @DisplayName("Update user by id")
    public void updateUserTestWithAllArguments(UserPOJO expectedUserForUpdate) {

        UserPOJO createdUser = userService.createRandomUser();
        String accessToken = authService.logIn(createdUser.getEmail(), createdUser.getPassword())
                .getJWTToken();

        UserPOJO actualUserForUpdate = userService
                .updateUser(createdUser.getId(), expectedUserForUpdate, accessToken)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(USER_JSON_SCHEMA))
                .extractAs(UserPOJO.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualUserForUpdate.getId()).isEqualTo(createdUser.getId());
            softly.assertThat(actualUserForUpdate.getName()).isEqualTo(expectedUserForUpdate.getName());
            softly.assertThat(actualUserForUpdate.getAvatar()).isEqualTo(expectedUserForUpdate.getAvatar());
            softly.assertThat(actualUserForUpdate.getEmail()).isEqualTo(expectedUserForUpdate.getEmail());
            softly.assertThat(actualUserForUpdate.getRole()).isEqualTo(expectedUserForUpdate.getRole());
            softly.assertThat(actualUserForUpdate.getPassword()).isEqualTo(expectedUserForUpdate.getPassword());
        });

        userService.deleteUser(createdUser.getId()).should(hasStatusCode(200));

    }

    @MethodSource(value = "test.fakeapi.data.DataFotTests#blankDataForUpdateUser")
    @ParameterizedTest()
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @Tag("NegativeTest")
    @DisplayName("Update user by id with blank arguments")
    public void updateUserWithBlankArgumentsTest(UserPOJO expectedUserForUpdate) {

        UserPOJO user = userService.createRandomUser();
        String accessToken = authService.logIn(user.getEmail(), user.getPassword()).getJWTToken();

        List<String> messageList = userService
                .updateUser(user.getId(), expectedUserForUpdate, accessToken)
                .should(hasStatusCode(400))
                .getMessageList();

        assertThat(messageList).isNotEmpty().allSatisfy(message -> {
            assertThat(message).containsAnyOf(MESSAGES);
        });

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

        UserPOJO user = userService.createRandomUser();
        String accessToken = authService.logIn(user.getEmail(), user.getPassword()).getJWTToken();


        List<String> listOfMessages = userService
                .updateUser(user.getId(), updatableUser, accessToken)
                .should(hasStatusCode(400))
                .getMessageList();


        assertThat(listOfMessages).isNotEmpty().allSatisfy(message -> {
            assertThat(message).containsAnyOf(MESSAGES);
        });

        userService.deleteUser(user.getId(), accessToken).should(hasStatusCode(200));
    }

    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetSingleUser")
    @Tag("UserTest")
    @Tag("Smoke")
    @DisplayName("Delete single user")
    public void deleteSingleUserTest() {

        UserPOJO user = userService.createRandomUser();
        String accessToken = authService.logIn(user.getEmail(), user.getPassword()).getJWTToken();

        boolean resultOfDelete = userService
                .deleteUser(user.getId(), accessToken)
                .should(hasStatusCode(200))
                .getResultOfDelete();


        assertThat(resultOfDelete).isTrue();

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

        String accessToken = authService.logInAdminUser().getJWTToken();
        String message = userService
                .deleteUser(userAdminId, accessToken)
                .should(hasStatusCode(401))
                .getMessage();

        assertThat(message).isEqualTo(ADMIN_IS_NOT_FOR_DELETE);

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

        boolean emailIsAvailable = userService
                .checkEmailIsAvailable(email)
                .should(hasStatusCode(201))
                .asJsonPath()
                .getBoolean("isAvailable");

        assertThat(emailIsAvailable).isTrue();
    }


    @Test
    @Tag("API")
    @Severity(SeverityLevel.MINOR)
    @Tag("CheckEmail")
    @Tag("UserTest")
    @DisplayName("Check if email is blank")
    public void blankEmailTest() {

        List<String> messageList = userService
                .checkEmailIsAvailable("")
                .should(hasStatusCode(400))
                .getMessageList();

        assertThat(messageList).isNotEmpty().allSatisfy(message -> {
            assertThat(message).containsAnyOf(MESSAGES);
        });

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
