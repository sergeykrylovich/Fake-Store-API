package test.fakeapi.requests;

import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import test.fakeapi.assertions.AssertableResponse;
import test.fakeapi.pojo.UserPOJO;

import java.util.*;

import static io.restassured.RestAssured.given;
import static test.fakeapi.data.UserData.getRandomUser;
import static test.fakeapi.specs.FakeStoreAPISpecs.prepareRequest;

@RequiredArgsConstructor
public class UserService {

    public static final String USER_BASE_PATH = "/users";
    public static final String USER_JSON_SCHEMA = "user-json-scheme.json";

    public static final String[] ROLES = {"admin", "customer"};

    public static final String PASS_LONGER_OR_EQUAL_4_CHARS = "password must be longer than or equal to 4 characters";
    public static final String ONLY_LETTERS_AND_NUMBERS = "password must contain only letters and numbers";
    public static final String AVATAR_MUST_BE_A_URL_ADDRESS = "avatar must be a URL address";


    @Step(value = "Create a random user")
    public UserPOJO createRandomUser() {

        UserPOJO user = getRandomUser();

        return given(prepareRequest(USER_BASE_PATH))
                .body(user)
                .when()
                .post("/")
                .then()
                .statusCode(201)
                .extract().as(UserPOJO.class);
    }

    @Step(value = "Create a single user")
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

    @Step(value = "Get all users with token")
    public AssertableResponse getAllUsers(String token) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .auth().oauth2(token)
                .when()
                .get("/")
                .then());
    }

    @Step(value = "Get single user without token")
    public AssertableResponse getSingleUser(int userId) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .pathParam("userId", userId)
                .when()
                .get("/{userId}")
                .then());
    }

    @Step(value = "Get single user with token")
    public AssertableResponse getSingleUser(int userId, String token) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .auth().oauth2(token)
                .pathParam("userId", userId)
                .when()
                .get("/{userId}")
                .then());
    }

    @Step(value = "Update single user")
    public AssertableResponse updateUser(int userId, String name, String email, String password, String avatar, String role) {

        UserPOJO updatableUser = new UserPOJO(name, email, password, role, avatar);

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .body(updatableUser)
                .pathParam("userId", userId)
                .when()
                .put("/{userId}")
                .then());
    }

    public AssertableResponse updateUser(int userId, UserPOJO updatableUser) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .body(updatableUser)
                .pathParam("userId", userId)
                .when()
                .put("/{userId}")
                .then());
    }

    @Step(value = "Update single user with token")
    public AssertableResponse updateUser(int userId, UserPOJO updatableUser, String token) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .auth().oauth2(token)
                .body(updatableUser)
                .pathParam("userId", userId)
                .when()
                .put("/{userId}")
                .then());
    }

    @Step(value = "Delete single user without token")
    public AssertableResponse deleteUser(int userId) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .pathParam("userId", userId)
                .when()
                .delete("/{userId}")
                .then());
    }

    @Step(value = "Delete single user with token")
    public AssertableResponse deleteUser(int userId, String token) {

        return new AssertableResponse(given(prepareRequest(USER_BASE_PATH))
                .auth().oauth2(token)
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
