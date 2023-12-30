package test.fakeapi.data;

import net.datafaker.Faker;
import test.fakeapi.pojo.UserPOJO;

import java.util.Random;

import static test.fakeapi.requests.UserService.ROLES;

public class RandomUserData {

    private static final Random random = new Random();
    private static final Faker faker = new Faker();

    public static UserPOJO getRandomUser(){
        return UserPOJO.builder()
                .name(faker.name().fullName() + random.nextInt(Integer.MAX_VALUE))
                .email(random.nextInt(Integer.MAX_VALUE) + faker.internet().emailAddress())
                .password(faker.internet().password(4, 12))
                .avatar(faker.internet().image())
                .role(ROLES[faker.random().nextInt(0, 1)])
                .build();
    }

    public static UserPOJO getUserWithBlankFields(){
        return new UserPOJO();
    }


}
