package resourcepaths;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ResourcePath {
    private static final int NUMBER_OF_ALLOWED_ARGUMENTS = 2;
    private static final Map<String, String> resourcePathMap;

    static {
        resourcePathMap = new HashMap<>();
        loadResourcePathMap("resource-paths");
    }

    private static void loadResourcePathMap(String resourceFilePath){
        InputStream is;
        InputStreamReader isr;
        BufferedReader br;

        try{
            is = ResourcePath.class.getClassLoader().getResourceAsStream(resourceFilePath);
            if (is == null){
                throw new FileNotFoundException("Resource file path\"" + resourceFilePath + "\" has not been found");
            }
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null){
                String[] arguments = line.split("=");
                validateArguments(arguments);
                populateResourcePathMap(arguments);
            }

        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Error while reading resourceFilePath " + resourceFilePath);
            System.exit(-1);
        }
    }

    private static void populateResourcePathMap(String[] arguments) {
        resourcePathMap.put(arguments[0].strip(), arguments[1].strip());
    }

    private static void validateArguments(String[] arguments) {
        try{
            validateNumberOfArguments(arguments);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    private static void validateNumberOfArguments(String[] arguments){
        if(arguments.length != 2){
            throw new IllegalArgumentException("Illegal number of arguments. It must be " + NUMBER_OF_ALLOWED_ARGUMENTS
                                                + ", but it was " + arguments.length);
        }
    }

    public static String getPathOf(String resource) {
        String resourcePath = resourcePathMap.get(resource);
        try{
            if(resourcePath == null){
                throw new FileNotFoundException("Resource \"" + resource + "\" has not been found");
            }
        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return resourcePath;
    }
}
