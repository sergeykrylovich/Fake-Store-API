package test.fakeapi.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.requests.AuthService;
import test.fakeapi.requests.FileService;
import test.fakeapi.utils.JsonHelper;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static test.fakeapi.assertions.Conditions.hasStatusCode;
import static test.fakeapi.specs.Constants.BASE_URL;

@Epic("Endpoints for working with files")
public class FileTests {

    @BeforeAll
    public static void setAllure() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void testFromFileJsonToObject() {
        CategoryPOJO categoryJSON = JsonHelper.fromJson("src/test/resources/file.json", CategoryPOJO.class);
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

        CategoryPOJO categoryJSON = JsonHelper.fromJsonString(cat, CategoryPOJO.class);
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
    public void testUploadTxtFile() {
        FileService fileService = new FileService();
        AuthService authService = new AuthService();
        String token = authService.createAndLoginRandomUser().getJWTToken();

        String fileName = "file.json";
        String fileExtension = fileName.substring(fileName.indexOf("."));
        JsonPath responseBody = fileService.uploadFile(fileName, token).should(hasStatusCode(201)).asJsonPath(); // to do
        String responseFileName = responseBody.getString("filename");

        System.out.println(fileExtension);
        System.out.println(responseFileName);

        SoftAssertions.assertSoftly(softly -> {
            assertThat(responseBody.getString("originalname")).isEqualTo(fileName);
            assertThat(responseFileName).endsWith(fileExtension);
            assertThat(responseBody.getString("location")).isEqualTo(BASE_URL + "/files/" + responseFileName);
        });
    }

    @Test
    @SneakyThrows
    public void testGetFile() {
        FileService fileService = new FileService();
        AuthService authService = new AuthService();
        String accessToken = authService.createAndLoginRandomUser().getJWTToken();
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
