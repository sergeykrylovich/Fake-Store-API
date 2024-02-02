package test.fakeapi.assertions;

import io.restassured.response.ValidatableResponse;

public interface Condition {

    void check(ValidatableResponse validatableResponse);
}
