package test.fakeapi.addons;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomTpl {

    private static final AllureRestAssured FILTER = new AllureRestAssured();

    private CustomTpl(){}

    public static CustomTpl initLogFilter() {
        return InitLoggerFilter.INITTPL;
    }

    public AllureRestAssured withCustomTemplate() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
        return FILTER;
    }

    private static class InitLoggerFilter{
        private static final CustomTpl INITTPL = new CustomTpl();
    }
}
