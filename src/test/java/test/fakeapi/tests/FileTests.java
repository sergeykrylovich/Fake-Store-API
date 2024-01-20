package test.fakeapi.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.pojo.FilePOJO;
import test.fakeapi.requests.AuthService;
import test.fakeapi.requests.BaseApi;
import test.fakeapi.requests.FileService;
import test.fakeapi.utils.JsonHelper;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.assertions.Conditions.hasStatusCode;

@Epic("Endpoints for working with files")
public class FileTests extends BaseApi {

    private FileService fileService;
    private AuthService authService;
    private String token;
    private

    @BeforeEach
    public void initTests() {
        authService = new AuthService();
        token = authService.createAndLoginRandomUser().getJWTToken();
        fileService = new FileService();
    }

    @Test
    public void testFromFileJsonToObject() {
        CategoryPOJO categoryJSON = JsonHelper.readJsonFromFilePath("src/test/resources/file.json", CategoryPOJO.class);
        System.out.println(categoryJSON.getImage());
        System.out.println(categoryJSON.getName());
        System.out.println(categoryJSON.getId());
    }

    @Test
    public void testFromJsonStringToObject() {
        CategoryPOJO categoryToJSON = CategoryPOJO
                .builder()
                .image("https://i.imgur.com/QkIa5tT.jpeg")
                .name("Valera")
                .id(122)
                .build();
        String cat = JsonHelper.toJson(categoryToJSON);

        CategoryPOJO categoryJSON = JsonHelper.readJsonFromString(cat, CategoryPOJO.class);
        System.out.println(categoryJSON.getImage());
        System.out.println(categoryJSON.getName());
        System.out.println(categoryJSON.getId());
    }

    @Test
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
        objectMapper.writeValue(new File("src/test/resources/file1.json"), cat);
        System.out.println(cat);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Tag("API")
    @Tag("FileTest")
    @Tag("Smoke")
    @DisplayName("Upload file")
    public void uploadFileTest() {
        String fileName = "Excel.xls";
        String fileExtension = "xls";
        String filePath = "src/test/resources/" + fileName;

        FilePOJO actualFileResponse = fileService.uploadFile(filePath, token)
                .should(hasStatusCode(201))
                .extractAs(FilePOJO.class);

        assertThat(actualFileResponse.getOriginalname()).isEqualTo(fileName);
        assertThat(actualFileResponse.getFilename()).isNotEmpty().endsWith(fileExtension);
        assertThat(actualFileResponse.getLocation()).isNotEmpty().endsWith(fileExtension);
    }

    @Test
    @SneakyThrows
    public void getFileTest() {
        String filename = fileService.uploadExampleFile(token)
                .extractAs(FilePOJO.class)
                .getFilename();
        fileService.getFile(filename, token);
       /* AssertableResponse file = fileService.getFile("532e.pdf"); // TO DO
        InputStream inputStream = file.asInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream("src/test/resources/532f.pdf");


        while (inputStream.available() > 0) {
            fileOutputStream.write(inputStream.readAllBytes());
            File uploadedFile = new File("src/test/resources/532e.pdf");
            File downloadedFile = new File("src/test/resources/532f.pdf");
            FileInputStream fileInputStream = new FileInputStream(uploadedFile);
            FileInputStream fileInputStream1 = new FileInputStream(downloadedFile);
            assertThat(IOUtils.contentEquals(fileInputStream, fileInputStream1)).isTrue();
            assertThat(uploadedFile.length()).isEqualTo(downloadedFile.length());

            IOUtils.close(fileInputStream1);
            IOUtils.close(inputStream);
            IOUtils.close(fileOutputStream);

            //Files.delete(Paths.get(downloadedFile.getPath()));
            downloadedFile.delete();
        }*/


    }

    @Test
    public void testDeleteFile() {


        File downloadedFile = new File("src/test/resources/532f.pdf");
        System.out.println(downloadedFile.delete());


    }
}
