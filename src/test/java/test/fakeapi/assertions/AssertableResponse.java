package test.fakeapi.assertions;

import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import test.fakeapi.pojo.ProductsPOJO;

import java.util.List;

@RequiredArgsConstructor
public class AssertableResponse {

    private final ValidatableResponse validatableResponse;

    public AssertableResponse should(Condition condition) {
        condition.check(validatableResponse);
        return this;
    }

    public <T> T extractAs(Class<T> tClass) {
        return validatableResponse.extract().as(tClass);
    }

    public <T> T extractAs(String jsonPath, Class<T> tClass) {
        return validatableResponse.extract().jsonPath().getObject(jsonPath, tClass);
    }

    public <T> List<T> asList(Class<T> tClass) {
        return validatableResponse.extract().jsonPath().getList("", tClass);
    }

    public <T> List<T> asList(String jsonPath, Class<T> tClass) {
        return validatableResponse.extract().jsonPath().getList(jsonPath, tClass);
    }

    public JsonPath asJsonPath() {
        return validatableResponse.extract().jsonPath();
    }
    public XmlPath asHtmlPath() {
        return validatableResponse.extract().htmlPath();
    }

    public boolean getResultOfDelete() {
        return validatableResponse.extract().htmlPath().getBoolean("html.body");
    }

    public List<String> getMessageList() {
        return validatableResponse.extract().jsonPath().getList("message");
    }

    public String getMessage() {
        return validatableResponse.extract().jsonPath().getString("message");
    }

    public String getJWTToken() {
        return validatableResponse.extract().jsonPath().getString("access_token");
    }

    public Integer getMaxIdOfProductResponse() {
        return validatableResponse.extract()
                .jsonPath()
                .getList("", ProductsPOJO.class)
                .stream()
                .mapToInt(x -> x.getId())
                .max()
                .orElse(1);
    }

}
