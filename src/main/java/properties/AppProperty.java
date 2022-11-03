package properties;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AppProperty {
    private static final int NUMBER_OF_ALLOWED_ARGUMENTS = 2;
    private static final Map<String, String> propertyPathMap;

    static {
        propertyPathMap = new HashMap<>();
        loadProperties("properties/app-property.init");
    }

    private static void loadProperties(String appPropertyFilePath){
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try{
            is = AppProperty.class.getClassLoader().getResourceAsStream(appPropertyFilePath);
            if (is == null){
                throw new FileNotFoundException("Resource file path\"" + appPropertyFilePath + "\" has not been found");
            }
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null){
                String[] arguments = line.split("=");
                validateArguments(arguments);
                addPropertyToMap(arguments);
            }

        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Error while reading the application property file path \"" + appPropertyFilePath + "\"");
            System.exit(-1);
        } finally {
            closeStreams(is, isr, br);
        }
    }

    private static void closeStreams(InputStream is, InputStreamReader isr, BufferedReader br){
        try {
            if(is != null){
                is.close();
            }
            if(isr != null){
                isr.close();
            }
            if(br != null){
                br.close();
            }
        } catch (IOException e) {
            System.out.println("Stream could not be closed");
        }
    }

    private static void addPropertyToMap(String[] arguments) {
        propertyPathMap.put(arguments[0].strip(), arguments[1].strip());
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

    public static String getResourcePath(String property) {
        String resourcePath = propertyPathMap.get(property);
        try{
            if(resourcePath == null){
                throw new FileNotFoundException("Resource path of \"" + property + "\" has not been found");
            }
        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return resourcePath;
    }
}
