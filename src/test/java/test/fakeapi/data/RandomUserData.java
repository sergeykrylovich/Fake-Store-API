package test.fakeapi.data;

import net.datafaker.Faker;
import test.fakeapi.pojo.UserPOJO;

import java.util.Random;

public class RandomUserData {

    private static final Random random = new Random();
    private static final Faker faker = new Faker();

    public static UserPOJO getRandomUser(){
        return UserPOJO.builder()
                .name(faker.name().fullName() + random.nextInt())
                .email(random.nextInt() + faker.internet().emailAddress())
                .password(faker.internet().password(4, 12))
                .avatar(faker.internet().image())
                .build();
    }


}
