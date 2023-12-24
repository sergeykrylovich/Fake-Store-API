package test.fakeapi.assertions.conditions;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import test.fakeapi.assertions.Condition;

@RequiredArgsConstructor
public class JsonSchemaCondition implements Condition {

    private final String JSON_SCHEMA;

    @Override
    public void check(ValidatableResponse validatableResponse) {
        validatableResponse.body(JsonSchemaValidator.matchesJsonSchemaInClasspath(JSON_SCHEMA));
    }


}
