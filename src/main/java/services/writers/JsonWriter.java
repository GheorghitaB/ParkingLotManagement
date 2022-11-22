package services.writers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;

import java.util.List;

public class JsonWriter<T> {
    public void writeOrAppendAsArray(File file, T object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        if(file.length() == 0) {
            objectMapper.writeValue(file, object);
        }
         else {
            String jsonObjectAsString = objectMapper.writeValueAsString(object);
            Object newObject = objectMapper.readValue(jsonObjectAsString, Object.class);

            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            List<Object> values = objectMapper.readValue(file, new TypeReference<>() {});
            values.add(newObject);

            objectMapper.writeValue(file, values);
        }
    }

    public void write(File file, List<T> list) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(file, list);
    }
}