package test.fakeapi.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import test.fakeapi.data.RandomUserData;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.BaseApi;
import test.fakeapi.requests.UserService;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.assertions.Conditions.*;
import static test.fakeapi.requests.UserService.*;

@Epic("API of User")
public class UsersTests extends BaseApi {

    protected static UserService userService;
    protected static Random random;
    protected static Faker faker;

/*    @BeforeAll
    public static void setUp() {
        random = new Random();
        faker = new Faker();
        userService = new UserService();
    }*/

    @BeforeEach
    public void initTests() {
        userService = new UserService();
        faker = new Faker();
        random = new Random();
    }



    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetAllUsers")
    @Tag("UserTest")
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
    @DisplayName("Get a single user by id")
    public void getSingleUserTest() {
        int userId = 1;
        UserPOJO user = userService
                .getSingleUser(userId)
                .should(hasStatusCode(200))
                .should(hasJsonSchema(USER_JSON_SCHEMA))
                .should(hasResponseTime(5l))
                .extractAs(UserPOJO.class);

        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("API")
    @Tag("CreateUser")
    @Tag("Integration")
    @Tag("UserTest")
    @DisplayName("Create new user")
    public void createUserTest() {

        UserPOJO user = RandomUserData.getRandomUser();

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
    public void updateUserTestWithAllArguments(String name, String email, String password, String avatar, String role) {

        UserPOJO user = userService
                .createRandomUser()
                .extractAs(UserPOJO.class);

        UserPOJO updatedUser = userService
                .updateUser(user.getId(), 200, name, email, password, avatar, role)
                .getObject("", UserPOJO.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getId()).as("User id").isEqualTo(updatedUser.getId());
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

/*        UserPOJO user = userService
                .createUser()
                .getObject("", UserPOJO.class);
        JsonPath errorUpdatedUser = userService
                .updateUser(user.getId(), 400, name, email, password, avatar, role);

        assertThat(errorUpdatedUser.getList("message").get(0)).isEqualTo("role must be one of the following values: admin, customer");*/

    }

    @MethodSource(value = "test.fakeapi.data.DataFotTests#dataForUpdateUserWithWrongAvatarAndPassword")
    @ParameterizedTest()
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("UpdateUser")
    @Tag("UserTest")
    @Tag("NegativeTest")
    @DisplayName("Update user by id without role")
    public void updateUserTestWithWrongFormatOfPasswordAndAvatar(String name, String email, String password, String avatar, String role) {

        UserPOJO user = userService
                .createRandomUser()
                .extractAs("", UserPOJO.class);

        JsonPath errorUpdatedUser = userService
                .updateUser(user.getId(), 400, name, email, password, avatar, role);


        assertThat(errorUpdatedUser.getList("message"))
                .contains(PASS_LONGER_OR_EQUAL_4_CHARS)
                .contains(ONLY_LETTERS_AND_NUMBERS)
                .contains(AVATAR_MUST_BE_A_URL_ADDRESS);
    }


    @Test
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("GetSingleUser")
    @Tag("UserTest")
    @DisplayName("Get single user")
    public void deleteSingleUserTest() {
/*        int userId = 1;
        UserPOJO user = userService
                .getSingleUser(userId)
                .getObject("", UserPOJO.class);

        assertThat(user.getId()).isEqualTo(userId);*/
    }

    @Test
    @Issue(value = "https://support.mycompany.by/JIRA-1")
    @Tag("API")
    @Severity(SeverityLevel.NORMAL)
    @Tag("CheckEmail")
    @Tag("UserTest")
    @DisplayName("Check that email is available")
    public void ExistingEmailTest() {

        String email = random.nextInt() + faker.internet().emailAddress();

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

        String invalidEmail = faker.internet().username();
        List<String> message = userService
                .checkEmailIsAvailable(invalidEmail)
                .should(hasStatusCode(400))
                .getMessageList();

        assertThat(message).contains("email must be an email");


    }
}
