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

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestUsers {

    public static final String userBasePATH = "/users";
    public static final String userSchema = "user-json-scheme.json";

    Faker faker = new Faker();

    @Step(value = "Create user")
    public UserPOJO createUser() {

        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(4, 8);
        String role = "admin";
        String avatar = faker.internet().image();
        UserPOJO user = new UserPOJO(name, email, password, role, avatar);

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(userBasePATH))
                .body(user)
                .when()
                .post("/")
                .then()
                .statusCode(SC_CREATED)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(userSchema))
                .extract().body().jsonPath().getObject("", UserPOJO.class);
    }

    @Step(value = "Get all users")
    public List<UserPOJO> getAllUsers() {

        //installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200, JSONSCHEME));

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(userBasePATH))
                .when()
                .get("/")
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(userSchema))
                .extract().body().jsonPath().getList("", UserPOJO.class);
    }

    @Step(value = "Get single user")
    public UserPOJO getSingleUser(int userId) {

        //installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200, userScheme));

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(userBasePATH))
                .when()
                .get("/" + userId)
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(userSchema))
                .extract().body().jsonPath().getObject("", UserPOJO.class);
    }

    @Step(value = "Update single user")
    public UserPOJO updateUser(int userId) {

        //installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200, userScheme));

        return given()
                .filters(new AllureRestAssured())
                .spec(requestSpecification(userBasePATH))
                .when()
                .put("/" + userId)
                .then()
                .statusCode(SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(userSchema))
                .extract().body().jsonPath().getObject("", UserPOJO.class);
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
