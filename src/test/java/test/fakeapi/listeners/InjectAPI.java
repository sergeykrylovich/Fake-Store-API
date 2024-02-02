package test.fakeapi.listeners;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import test.fakeapi.requests.AuthService;
import test.fakeapi.tests.CategoriesTests;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InjectAPI implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        testInstance.getClass()
                .getDeclaredField("authService")
                .set(testInstance, new AuthService());
        AuthService authService = (AuthService) testInstance.getClass().getDeclaredField("authService").get(testInstance);
        testInstance.getClass()
                .getDeclaredField("token")
                .set(testInstance, authService.createAndLoginRandomUser().getJWTToken());
    }
}
