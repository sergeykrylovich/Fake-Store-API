package test.fakeapi.requests;

import io.qameta.allure.Step;
import test.fakeapi.assertions.AssertableResponse;
import test.fakeapi.pojo.UserPOJO;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static test.fakeapi.data.UserData.getAdminUser;
import static test.fakeapi.specs.FakeStoreAPISpecs.prepareRequest;

public class AuthService {

    private static final String AUTH_LOGIN_PATH = "/auth/login";
    private static final String AUTH_PROFILE_PATH = "/auth/profile";
    private static final String REFRESH_TOKEN_PATH = "/auth/refresh-token";

    @Step("Get access token by credentials")
    public AssertableResponse logIn(String email, String password) {

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("email", email);
        loginMap.put("password", password);

        return new AssertableResponse(given(prepareRequest(AUTH_LOGIN_PATH))
                .body(loginMap)
                .when()
                .post()
                .then());
    }

    @Step("Get access token by credentials")
    public AssertableResponse logIn(UserPOJO user) {

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("email", user.getEmail());
        loginMap.put("password", user.getPassword());

        return new AssertableResponse(given(prepareRequest(AUTH_LOGIN_PATH))
                .body(loginMap)
                .when()
                .post()
                .then());
    }

    @Step("Login with random user")
    public AssertableResponse createAndLoginRandomUser() {
        UserService userService = new UserService();
        UserPOJO user = userService.createRandomUser();

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("email", user.getEmail());
        loginMap.put("password", user.getPassword());

        return new AssertableResponse(given(prepareRequest(AUTH_LOGIN_PATH))
                .body(user)
                .when()
                .post()
                .then());
    }

    @Step("Login with admin")
    public AssertableResponse logInAdminUser() {

        UserPOJO adminUser = getAdminUser();
 /*       Map<String, String> loginMap = new HashMap<>();
        loginMap.put("email", adminUser.getEmail());
        loginMap.put("password", adminUser.getPassword());*/

        return new AssertableResponse(given(prepareRequest(AUTH_LOGIN_PATH))
                .body(adminUser)
                .when()
                .post()
                .then()
                .statusCode(201));
    }

    @Step("Get user by JWT token")
    public AssertableResponse getUserByJWTToken(String token) {

        Map<String, String> authToken = new HashMap<>();
        authToken.put("Authorization", "Bearer " + token);


        return new AssertableResponse(given(prepareRequest(AUTH_PROFILE_PATH))
                .auth().oauth2(token)
                .body(authToken)
                .when()
                .get()
                .then());
    }

    @Step("Get new JWT token by refresh token")
    public AssertableResponse getNewTokenByRefreshToken(String refreshToken) {

        Map<String, String> refreshTokenMap = new HashMap<>();
        refreshTokenMap.put("refreshToken", refreshToken);

        return new AssertableResponse(given(prepareRequest(REFRESH_TOKEN_PATH))
                .body(refreshTokenMap)
                .when()
                .post()
                .then());
    }


}
