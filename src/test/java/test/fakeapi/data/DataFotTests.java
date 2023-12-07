package test.fakeapi.data;

import net.datafaker.Faker;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class DataFotTests {

    public static Stream<Arguments> dataForUpdateUser() {
        String[] roles = {"admin", "customer"};

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
}
