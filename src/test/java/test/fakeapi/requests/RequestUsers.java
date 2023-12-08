package test.fakeapi.requests;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import net.datafaker.Faker;
import test.fakeapi.pojo.UserPOJO;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.lessThan;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestUsers {

    public static final String userBasePATH = "/users";
    public static final String userSchema = "user-json-scheme.json";

    public static final String[] roles = {"admin", "customer"};
    public static final String passwordLongerMessage = "password must be longer than or equal to 4 characters";
    public static final String passwordOnlyNumbersAndLettersMessage = "password must contain only letters and numbers";
    public static final String avatarMustBeURLMessage = "avatar must be a URL address";

    Faker faker = new Faker();

    @Step(value = "Create user")
    public JsonPath createUserWithoutArguments() {

        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(4, 8);
        String role = roles[faker.random().nextInt(0, 1)];
        String avatar = faker.internet().image();
        UserPOJO user = new UserPOJO(name, email, password, role, avatar);

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(userBasePATH))
                .body(user)
                .log().body()
                .when()
                .post("/")
                .then()
                .statusCode(SC_CREATED)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(userSchema))
                .log().body()
                .extract().jsonPath();
    }

    @Step(value = "Get all users")
    public JsonPath getAllUsers() {

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(userBasePATH))
                .when()
                .get("/")
                .then()
                .statusCode(SC_OK)
                .time(lessThan(8l), TimeUnit.SECONDS)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(userSchema))
                .extract().jsonPath();
    }

    @Step(value = "Get single user")
    public JsonPath getSingleUser(int userId) {

        //installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200, userScheme));

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(userBasePATH))
                .when()
                .get("/" + userId)
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(userSchema))
                .log().body()
                .extract().jsonPath();
    }

    @Step(value = "Update single user")
    public JsonPath updateUser(int userId,int statusCode, String name, String email, String password, String avatar, String role) {

        //installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200, userScheme));
        UserPOJO updatableUser = new UserPOJO(name, email, password, role, avatar);

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(userBasePATH))
                .body(updatableUser)
                .when()
                .put("/" + userId)
                .then()
                .statusCode(statusCode)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(userSchema))
                .extract().jsonPath();
    }

    @Step(value = "Check email of user")
    public JsonPath checkEmail(String email) {

        //installSpecification(requestSpecification(USERBASEPATH), responseSpecification(201));

        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("email", email);


        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(userBasePATH))
                .body(emailMap)
                .when()
                .post("/is-available")
                .then()
                .statusCode(SC_CREATED)
                .extract().body().jsonPath();
    }


}
