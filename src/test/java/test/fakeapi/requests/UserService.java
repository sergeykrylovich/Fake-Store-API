package test.fakeapi.requests;

import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import net.datafaker.Faker;
import test.fakeapi.assertions.AssertableResponse;
import test.fakeapi.pojo.UserPOJO;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static test.fakeapi.data.RandomUserData.getRandomUser;
import static test.fakeapi.specs.FakeStoreAPISpecs.prepareRequest;

public class UserService {

    public static final String USER_BASE_PATH = "/users";
    public static final String USER_JSON_SCHEMA = "user-json-scheme.json";

    public static final String[] ROLES = {"admin", "customer"};
    public static final String PASS_LONGER_OR_EQUAL_4_CHARS = "password must be longer than or equal to 4 characters";
    public static final String ONLY_LETTERS_AND_NUMBERS = "password must contain only letters and numbers";
    public static final String AVATAR_MUST_BE_A_URL_ADDRESS = "avatar must be a URL address";

    Faker faker = new Faker();

    @Step(value = "Create random user user")
    public AssertableResponse createRandomUser() {

        UserPOJO user = getRandomUser();

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .body(user)
                .when()
                .post("/")
                .then());
    }

    @Step(value = "Create single user")
    public AssertableResponse createUser(UserPOJO user) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .body(user)
                .when()
                .post("/")
                .then());
    }

    @Step(value = "Get all users")
    public AssertableResponse getAllUsers() {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .when()
                .get("/")
                .then());
    }

    @Step(value = "Get single user")
    public AssertableResponse getSingleUser(int userId) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .pathParam("userId", userId)
                .when()
                .get("/{userId}")
                .then());
    }

    @Step(value = "Update single user")
    public JsonPath updateUser(int userId, int statusCode, String name, String email, String password, String avatar, String role) {

        UserPOJO updatableUser = new UserPOJO(name, email, password, role, avatar);

        return given(prepareRequest(USER_BASE_PATH))
                .body(updatableUser)
                .pathParam("userId", userId)
                .when()
                .put("/{userId}")
                .then()
                .statusCode(statusCode)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(USER_JSON_SCHEMA))
                .extract().jsonPath();
    }

    @Step(value = "Update single user")
    public AssertableResponse deleteUser(int userId) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .pathParam("userId", userId)
                .when()
                .delete("/{userId}")
                .then());
    }


    @Step(value = "Check email of user")
    public AssertableResponse checkEmailIsAvailable(String email) {

        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("email", email);

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .body(emailMap)
                .log().all()
                .when()
                .post("/is-available")
                .then());

    }

}
