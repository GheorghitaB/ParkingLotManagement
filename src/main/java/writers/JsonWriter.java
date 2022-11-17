package writers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonWriter {
    public void write(File file, Object object) throws IOException {
        new ObjectMapper().writeValue(file, object);
    }
}
