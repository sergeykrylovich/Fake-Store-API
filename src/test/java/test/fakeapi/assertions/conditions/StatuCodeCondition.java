package test.fakeapi.assertions.conditions;

import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import test.fakeapi.assertions.Condition;

@RequiredArgsConstructor
public class StatuCodeCondition implements Condition {

    private final Integer statusCode;
    @Override
    public void check(ValidatableResponse validatableResponse) {
       int actualStatusCode =  validatableResponse.extract().statusCode();
       Assertions.assertThat(actualStatusCode).isEqualTo(statusCode);
    }
}
