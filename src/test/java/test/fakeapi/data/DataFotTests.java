package test.fakeapi.data;

import net.datafaker.Faker;
import org.junit.jupiter.params.provider.Arguments;
import test.fakeapi.pojo.UserPOJO;

import java.util.stream.Stream;

import static test.fakeapi.requests.UserService.*;

public class DataFotTests {

    public static Stream<Arguments> dataForUpdateUser() {

        UserPOJO user = UserData.getRandomUser();

        return Stream.of(Arguments.of(user));
    }

    public static Stream<Arguments> blankDataForUpdateUser() {

        UserPOJO user = UserPOJO.builder()
                .name("")
                .email("")
                .password("")
                .avatar("")
                .role("")
                .build();

        return Stream.of(Arguments.of(user));
    }

    public static Stream<Arguments> dataForUpdateUserWithWrongAvatarAndPassword() {

        Faker faker = new Faker();
        UserPOJO userPOJO = UserPOJO.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.emoji().smiley())
                .avatar(faker.text().text(1, 3))
                .role(ROLES[faker.random().nextInt(0, 1)])
                .build();
        return Stream.of(Arguments.of(userPOJO));
    }
}
