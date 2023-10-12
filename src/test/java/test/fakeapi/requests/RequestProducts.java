package test.fakeapi.requests;


import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.*;

public class RequestProducts {
    public final String BASEPATH = "/products";
    @Test
    public Response getAllProducts() {

        installSpecification(requestSpecification(BASEPATH), responseSpecification());

        return given()
                .when()
                .get()
                .then()
                .log().all()
                .extract().response();


    }




}
