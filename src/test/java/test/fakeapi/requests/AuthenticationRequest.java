package test.fakeapi.requests;

import io.qameta.allure.Step;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class AuthenticationRequest {

    private static final String AUTHPATH = "/auth/login";

    @Step("Get access token by credentials")
    public static String getAccessToken() {
        installSpecification(requestSpecification(AUTHPATH), responseSpecification1(201));

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("email", "john@mail.com");
        loginMap.put("password", "changeme");

        return given()
                .body(loginMap)
                .when()
                .post()
                .then()
                .extract().body().jsonPath().getString("access_token");
    }

}
