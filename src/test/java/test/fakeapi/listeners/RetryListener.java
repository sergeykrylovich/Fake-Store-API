package test.fakeapi.listeners;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class RetryListener implements TestExecutionExceptionHandler, AfterTestExecutionCallback {

    private static final int MAX_RETRIES = 3;
    private static final Set<String> failedTestNames = new HashSet<>();
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                context.getRequiredTestMethod().invoke(context.getRequiredTestInstance());
                return;
            } catch (Throwable e) {
                throwable = e.getCause();
            }
        }
        throw throwable;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        String methodName = context.getRequiredTestMethod().getName();
        String className = context.getRequiredTestClass().getName();
        String testToWrite = String.format("--tests %s.%s*", className, methodName);
        context.getExecutionException().ifPresent(x-> failedTestNames.add(testToWrite));


    }

    @SneakyThrows
    public static void saveFailedTests() {
        String outputPath = "src/test/resources/FailedTest.txt";
        String result = String.join(",", failedTestNames);
        FileUtils.writeStringToFile(new File(outputPath), result);
    }
}
