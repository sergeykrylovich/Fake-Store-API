package test.fakeapi.assertions.conditions;

import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import test.fakeapi.assertions.Condition;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

@RequiredArgsConstructor
public class ResponseTimeCondition implements Condition {

    private final long TIME;
    @Override
    public void check(ValidatableResponse validatableResponse) {
        validatableResponse.time(lessThan(TIME), TimeUnit.SECONDS);
    }
}
