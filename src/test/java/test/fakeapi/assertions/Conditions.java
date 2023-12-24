package test.fakeapi.assertions;

import test.fakeapi.assertions.conditions.JsonSchemaCondition;
import test.fakeapi.assertions.conditions.MessageCondition;
import test.fakeapi.assertions.conditions.ResponseTimeCondition;
import test.fakeapi.assertions.conditions.StatuCodeCondition;

public class Conditions {

    public static StatuCodeCondition hasStatusCode(Integer expectedStatus) {
        return new StatuCodeCondition(expectedStatus);
    }

    public static MessageCondition hasMessage(String expectedMessage) {
        return new MessageCondition(expectedMessage);
    }
    public static JsonSchemaCondition hasJsonSchema(String jsonSchema) {
        return new JsonSchemaCondition(jsonSchema);
    }

    public static ResponseTimeCondition hasResponseTime(long responseTime) {
        return new ResponseTimeCondition(responseTime);
    }

}
