package test.fakeapi.requests;

import io.qameta.allure.Step;
import test.fakeapi.assertions.AssertableResponse;

import java.io.File;

import static io.restassured.RestAssured.given;
import static test.fakeapi.specs.FakeStoreAPISpecs.prepareRequestForUploadFile;

public class FileService {

    public static final String UPLOAD_FILE_PATH = "/files/upload";
    public static final String FILES_PATH = "/files";
    public static final String EXAMPLE_FILE = "src/test/resources/Excel.xls";


    @Step(value = "Uploading file with token")
    public AssertableResponse uploadFile(String filePath, String token) {

        return new AssertableResponse(given(prepareRequestForUploadFile(UPLOAD_FILE_PATH))
                .auth().oauth2(token)
                .multiPart(new File(filePath))
                .when()
                .post()
                .then());
    }

    @Step(value = "Uploading example file with token")
    public AssertableResponse uploadExampleFile(File exampleFile, String token) {

        return new AssertableResponse(given(prepareRequestForUploadFile(UPLOAD_FILE_PATH))
                .auth().oauth2(token)
                .multiPart(exampleFile)
                .when()
                .post()
                .then());
    }

    @Step(value = "Get a file with token")
    public AssertableResponse getFile(String fileName, String token) {

        return new AssertableResponse(given(prepareRequestForUploadFile(FILES_PATH))
                .auth().oauth2(token)
                .pathParam("fileName", fileName)
                .when()
                .get("/{fileName}")
                .then());
    }

}
