package test.fakeapi.requests;

import io.qameta.allure.Step;
import test.fakeapi.assertions.AssertableResponse;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.prepareRequest;

public class AuthService {

    private static final String AUTH_PATH = "/auth/login";

    @Step("Get access token by credentials")
    public AssertableResponse logIn(String email, String password) {

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("email", email);
        loginMap.put("password", password);

        return new AssertableResponse(given(prepareRequest(AUTH_PATH))
                .body(loginMap)
                .when()
                .post()
                .then());
    }


}
