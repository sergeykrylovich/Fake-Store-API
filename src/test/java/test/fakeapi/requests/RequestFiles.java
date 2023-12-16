package test.fakeapi.requests;

import io.restassured.path.json.JsonPath;
import test.fakeapi.specs.FakeStoreAPISpecs;

import java.io.File;

import static io.restassured.RestAssured.given;

public class RequestFiles {

    public static final String UPLOAD_FILE_PATH = "/files/upload";
    public static final String FILE_PATH = "/files";


    public static JsonPath uploadFile(String fileName) {

        return given()
                .spec(FakeStoreAPISpecs.requestSpecificationForUploadFile(UPLOAD_FILE_PATH))
                .multiPart(new File("src/test/resources/" + fileName))
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().jsonPath();
    }

    public static JsonPath getFile(String fileName) {

        return given()
                .spec(FakeStoreAPISpecs.requestSpecificationForUploadFile(FILE_PATH))
                .when()
                .get("/" + fileName)
                .then()
                .statusCode(200)
                .extract().jsonPath();
    }


}
