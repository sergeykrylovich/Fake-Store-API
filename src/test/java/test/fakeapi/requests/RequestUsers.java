package test.fakeapi.requests;

import io.qameta.allure.Step;
import net.datafaker.Faker;
import test.fakeapi.pojo.UserPOJO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestUsers {

    private static final String USERBASEPATH = "/users";
    Faker faker = new Faker();

    @Step(value = "Create user")
    public UserPOJO createUser() {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(201));

        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(4, 8);
        String role = "admin";
        String avatar = faker.internet().image();
        UserPOJO user = new UserPOJO(name, email, password, role, avatar, null, null, null);

        return given()
                .body(user)
                .when()
                .post("/")
                .then()
                .log().all()
                .extract().body().jsonPath().getObject("", UserPOJO.class);
    }
    @Step(value = "Get all users")
    public List<UserPOJO> getAllUsers() {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200));


        return given()
                .when()
                .get("/")
                .then()
                .log().all()
                .extract().body().jsonPath().getList("", UserPOJO.class);
    }
    @Step(value = "Get single user")
    public UserPOJO getSingleUser(int userId) {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200));


        return given()
                .when()
                .get("/" + userId)
                .then()
                .log().all()
                .extract().body().jsonPath().getObject("", UserPOJO.class);
    }
    @Step(value = "Update single user")
    public UserPOJO updateUser(int userId) {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(200));


        return given()
                .when()
                .get("/" + userId)
                .then()
                .log().all()
                .extract().body().jsonPath().getObject("", UserPOJO.class);
    }

    @Step(value = "Check email of user")
    public Boolean checkEmail() {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(201));
        Map<String, String> email = new HashMap<>();
        email.put("email", "john@mail.com");


        return given()
                .body(email)
                .when()
                .post("/is-available")
                .then()
                .log().all()
                .extract().body().jsonPath().get("isAvailable");
    }


}
