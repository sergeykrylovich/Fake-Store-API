package test.fakeapi.data;

import io.qameta.allure.Step;
import net.datafaker.Faker;
import test.fakeapi.pojo.UserPOJO;

import java.util.Random;

import static test.fakeapi.requests.UserService.ROLES;

public class UserData {

    private static final Random random = new Random();
    private static final Faker faker = new Faker();

    private static final String ADMIN_EMAIL = "john@mail.com";
    private static final String ADMIN_PASSWORD = "changeme";

    @Step(value = "Creating a random user object")
    public static UserPOJO getRandomUser(){
        return UserPOJO.builder()
                .name(faker.name().fullName() + random.nextInt(Integer.MAX_VALUE))
                .email(random.nextInt(Integer.MAX_VALUE) + faker.internet().emailAddress())
                .password(faker.internet().password(4, 12))
                .avatar(faker.internet().image())
                .role(ROLES[faker.random().nextInt(0, 1)])
                .build();
    }

    @Step(value = "Getting a admin user")
    public static UserPOJO getAdminUser(){
        return UserPOJO.builder()
                .email(ADMIN_EMAIL)
                .password(ADMIN_PASSWORD)
                .build();
    }

    public static UserPOJO getUserWithBlankFields(){
        return new UserPOJO();
    }


}
