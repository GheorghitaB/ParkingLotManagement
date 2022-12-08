package services.readers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader<T> {
    public T readJson(String json, Class<T> objectType) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, objectType);
    }
}
