package test.fakeapi.requests;

import test.fakeapi.pojo.UserPOJO;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestUsers {

    private static final String USERBASEPATH = "/users";

    public UserPOJO createUser() {

        installSpecification(requestSpecification(USERBASEPATH), responseSpecification(201));

        UserPOJO user = new UserPOJO("sergioGusto", "serg@gmail.com", "1111", "admin", "https://api.lorem.space/image/face?w=640&h=480&r=867", null, null, null);

        return given()
                .body(user)
                .when()
                .post("/")
                .then()
                .log().all()
                .extract().body().jsonPath().getObject("", UserPOJO.class);
    }


}
