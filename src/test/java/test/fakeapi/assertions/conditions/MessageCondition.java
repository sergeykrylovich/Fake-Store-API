package test.fakeapi.assertions.conditions;

import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import test.fakeapi.assertions.Condition;
import test.fakeapi.pojo.InfoMessage;

@RequiredArgsConstructor
public class MessageCondition implements Condition {

    private final String expectedMessage;
    @Override
    public void check(ValidatableResponse validatableResponse) {
        InfoMessage info = validatableResponse.extract().jsonPath().getObject("", InfoMessage.class);
        Assertions.assertThat(info.getMessage()).isEqualTo(expectedMessage);
    }
}
