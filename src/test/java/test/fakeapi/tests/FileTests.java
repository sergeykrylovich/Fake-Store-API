package test.fakeapi.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.CategoryPOJO;
import test.fakeapi.requests.RequestFiles;
import test.fakeapi.utils.JsonHelper;

import java.io.File;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
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
        String fileName = "file.json";
        String fileExtension = fileName.substring(fileName.indexOf("."));
        JsonPath responseBody = RequestFiles.uploadFile(fileName);
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
        FileInputStream fileInputStream = new FileInputStream(new File("src/test/resources/file.json"));
        FileInputStream fileInputStream1 = new FileInputStream(new File("src/test/resources/file1.json"));
        assertThat(IOUtils.contentEquals(fileInputStream, fileInputStream1)).isTrue();

    }
}
