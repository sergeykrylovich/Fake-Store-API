package test.fakeapi.data;

import net.datafaker.Faker;
import org.junit.jupiter.params.provider.Arguments;
import test.fakeapi.requests.RequestUsers;

import java.util.stream.Stream;

import static test.fakeapi.requests.RequestUsers.*;

public class DataFotTests {

    public static Stream<Arguments> dataForUpdateUser() {

        Faker faker = new Faker();
        return Stream.of(Arguments.of(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.internet().password(4,8),
                faker.internet().image(),
                roles[faker.random().nextInt(0, 1)]));
    }
    public static Stream<Arguments> dataForUpdateUserNegative() {


        Faker faker = new Faker();
        return Stream.of(Arguments.of(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.internet().password(4,8),
                faker.internet().image(),
                ""));
    }

    public static Stream<Arguments> dataForUpdateUserWithWrongAvatarAndPassword() {

        Faker faker = new Faker();
        return Stream.of(Arguments.of(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.emoji().smiley(),
                faker.text().text(1, 3),
                roles[faker.random().nextInt(0, 1)]));
    }
}
