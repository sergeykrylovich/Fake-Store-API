package test.fakeapi.requests;

import test.fakeapi.assertions.AssertableResponse;

import java.io.File;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.prepareRequestForUploadFile;

public class FileService {

    public static final String UPLOAD_FILE_PATH = "/files/upload";
    public static final String FILE_PATH = "/files";


    public AssertableResponse uploadFile(String fileName, String token) {

        return new AssertableResponse(given(prepareRequestForUploadFile(UPLOAD_FILE_PATH))
                .auth().oauth2(token)
                .multiPart(new File("src/test/resources/" + fileName))
                .when()
                .post()
                .then());
    }

    public AssertableResponse getFile(String fileName) {

        return new AssertableResponse(given(prepareRequestForUploadFile(FILE_PATH))
                .pathParam("fileName", fileName)
                .when()
                .get("/{fileName}")
                .then());
    }


}
