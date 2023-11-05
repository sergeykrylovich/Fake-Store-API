package test.fakeapi.tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.fakeapi.pojo.UserPOJO;
import test.fakeapi.requests.RequestUsers;

public class UsersTests {

    RequestUsers requestUsers = new RequestUsers();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("CreateUser")
    @DisplayName("Create new user")
    public void createUserTest() {
        UserPOJO user = requestUsers.createUser();

        System.out.println(String.format("Id = %s, email = %s, name = %s, password = %s, role = %s",
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                user.getRole()));
    }
}
