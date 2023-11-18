package test.fakeapi.requests;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import net.datafaker.Faker;
import test.fakeapi.pojo.UserPOJO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestUsers {

    public static final String USERBASEPATH = "/users";
    public static final String JSONSCHEME = "user-json-scheme.json";
    Faker faker = new Faker();

    @Step(value = "Create user")
    public UserPOJO createUser() {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(201, JSONSCHEME));

        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(4, 8);
        String role = "admin";
        String avatar = faker.internet().image();
        UserPOJO user = new UserPOJO(name, email, password, role, avatar);

        return given()
                .body(user)
                .when()
                .post("/")
                .then()
                .extract().body().jsonPath().getObject("", UserPOJO.class);
    }

    @Step(value = "Get all users")
    public List<UserPOJO> getAllUsers() {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200, JSONSCHEME));

        return given()
                .when()
                .get("/")
                .then()
                .extract().body().jsonPath().getList("", UserPOJO.class);
    }

    @Step(value = "Get single user")
    public UserPOJO getSingleUser(int userId) {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200, JSONSCHEME));

        return given()
                .when()
                .get("/" + userId)
                .then()
                .extract().body().jsonPath().getObject("", UserPOJO.class);
    }

    @Step(value = "Update single user")
    public UserPOJO updateUser(int userId) {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200, JSONSCHEME));

        return given()
                .when()
                .put("/" + userId)
                .then()
                .extract().body().jsonPath().getObject("", UserPOJO.class);
    }

    @Step(value = "Check email of user")
    public JsonPath checkEmail(String email) {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(201));

        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("email", email);


        return given()
                .body(emailMap)
                .when()
                .post("/is-available")
                .then()
                .extract().body().jsonPath();
    }


}
