package test.fakeapi.requests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import test.fakeapi.addons.CustomTpl;
import test.fakeapi.listeners.SaveFailedTests;
import test.fakeapi.specs.Constants;

public class BaseApi {


    @BeforeAll
    public static void setUp() {

        RestAssured.filters(new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                CustomTpl.withCustomTemplate());
        RestAssured.baseURI = Constants.BASE_URL;
    }
    @AfterAll
    public static void savedFailedTest() {
        SaveFailedTests.saveFailedTests();
    }


}
