package test.hvlProject.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public class Mapper {

    private static ObjectMapper OBJECT_MAPPER;

    private Mapper() {}

    public static synchronized ObjectMapper getInstance() {
        if (Objects.isNull(OBJECT_MAPPER)) {
             OBJECT_MAPPER = new ObjectMapper();
        }
        return OBJECT_MAPPER;
    }

    public <T> T readValue(String value, TypeReference<T> type) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(value, type);
    }

    public <T> String writeValueAsString(T value) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(value);
    }
}
