package test.fakeapi.requests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import test.fakeapi.addons.CustomTpl;

public class BaseApi {


    @BeforeAll
    public static void setUp() {
        RestAssured.filters(new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                CustomTpl.withCustomTemplate());
    }

}
