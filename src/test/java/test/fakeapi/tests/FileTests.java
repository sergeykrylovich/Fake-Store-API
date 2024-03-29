package test.fakeapi.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import test.fakeapi.listeners.SaveFailedTests;
import test.fakeapi.listeners.Smoke;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.pojo.FilePOJO;
import test.fakeapi.requests.AuthService;
import test.fakeapi.requests.BaseApi;
import test.fakeapi.requests.FileService;
import test.fakeapi.utils.JsonHelper;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.assertions.Conditions.hasStatusCode;
import static test.fakeapi.requests.FileService.EXAMPLE_FILE;
@ExtendWith(SaveFailedTests.class)
@Epic("Endpoints for working with files")
public class FileTests extends BaseApi {

    private FileService fileService;
    private String token;


    @BeforeEach
    public void initTests() {
        AuthService authService = new AuthService();
        token = authService.createAndLoginRandomUser().getJWTToken();
        fileService = new FileService();
    }

    @Test
    @Tag("ExampleTestJSON")
    public void testFromFileJsonToObject() {
        CategoryPOJO categoryJSON = JsonHelper.readJsonFromFilePath("src/test/resources/file.json", CategoryPOJO.class);
        assertThat(categoryJSON.getImage()).isNotEmpty();
        assertThat(categoryJSON.getId()).isPositive();
        assertThat(categoryJSON.getName()).isNotEmpty();
    }

    @Test
    @Tag("ExampleTestJSON")
    public void testFromJsonStringToObject() {
        CategoryPOJO categoryToJSON = CategoryPOJO
                .builder()
                .image("https://i.imgur.com/QkIa5tT.jpeg")
                .name("Valera")
                .id(122)
                .build();
        String cat = JsonHelper.toJson(categoryToJSON);

        assertThat(cat).isNotEmpty();

        CategoryPOJO categoryJSON = JsonHelper.readJsonFromString(cat, CategoryPOJO.class);
        assertThat(categoryJSON.getImage()).isNotEmpty();
        assertThat(categoryJSON.getId()).isPositive();
        assertThat(categoryJSON.getName()).isNotEmpty();
    }

    @Test
    @Tag("ExampleTestJSON")
    @SneakyThrows
    public void testFromObjectToJson() {
        CategoryPOJO categoryToJSON = CategoryPOJO
                .builder()
                .image("https://i.imgur.com/QkIa5tT.jpeg")
                .name("Valera")
                .id(122)
                .build();
        String cat = JsonHelper.toJson(categoryToJSON);
        ObjectMapper objectMapper = new ObjectMapper();
        File expectedFile = new File("src/test/resources/file1.json");
        objectMapper.writeValue(expectedFile, cat);

        assertThat(expectedFile).isNotEmpty();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("FileTest")
    @Smoke
    @DisplayName("Upload excel file from resources to API")
    public void uploadFileTest() {
        String filePath = EXAMPLE_FILE;
        String fileName = filePath.split("/")[3];
        String fileExtension = filePath.substring(filePath.indexOf("."));

        FilePOJO actualFileResponse = fileService.uploadFile(filePath, token)
                .should(hasStatusCode(201))
                .extractAs(FilePOJO.class);

        assertThat(actualFileResponse.getOriginalname()).isEqualTo(fileName);
        assertThat(actualFileResponse.getFilename()).isNotEmpty().endsWith(fileExtension);
        assertThat(actualFileResponse.getLocation()).isNotEmpty().endsWith(fileExtension);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("FileTest")
    @Smoke
    @DisplayName("Get a downloaded file and compare with expected file")
    @SneakyThrows
    public void getFileTest() {

        File expectedFile = new File(EXAMPLE_FILE);

        String filename = fileService.uploadExampleFile(expectedFile, token)
                .extractAs(FilePOJO.class)
                .getFilename();
        File actualFile = new File("src/test/resources/" + filename);

        byte[] actualFileWithByteArray = fileService.getFile(filename, token)
                .should(hasStatusCode(200))
                .asByteArray();
        FileUtils.writeByteArrayToFile(actualFile, actualFileWithByteArray);

        assertThat(FileUtils.contentEquals(actualFile, expectedFile)).isTrue();

        FileUtils.deleteQuietly(actualFile);
    }
}
