package test.fakeapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;

public class JsonHelper {

    private static final ObjectMapper objectmapper = new ObjectMapper();


    @SneakyThrows
    public static String toJson(Object object) {
        return objectmapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T readJsonFromFilePath(String filePath, Class<T> tClass) {

        File file = new File(filePath);
        return objectmapper.readValue(file, tClass);
    }

    @SneakyThrows
    public static <T> T readJsonFromString(String json, Class<T> tClass) {

        return objectmapper.readValue(json, tClass);
    }
}
