package services.properties;

import utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.parsers.TextArgumentParser.*;

public class AppProperty {
    private static final int NUMBER_OF_ALLOWED_ARGUMENTS = 2;
    private static final Map<String, String> propertiesMap;
    private static final String SPLIT_REGEX = "=";

    static {
        propertiesMap = new HashMap<>();
        loadPropertiesFromFile();
    }

    private static void loadPropertiesFromFile(){
        List<String> lines = readLines(Constants.APP_PROPERTIES_FILEPATH);
        lines.forEach(line -> {
            String[] arguments = getArgumentsFromLine(line, SPLIT_REGEX);
            validateArguments(arguments);
            arguments = prepareArguments(arguments);
            propertiesMap.put(arguments[0], arguments[1]);
        });
    }

    private static void validateArguments(String[] arguments) {
        if(arguments.length != 2){
            System.out.println("Illegal number of arguments. It must be " + NUMBER_OF_ALLOWED_ARGUMENTS
                    + ", but it was " + arguments.length);
            System.exit(-1);
        }
    }

    public static String getProperty(String property) {
        String propertyValue = propertiesMap.get(property);
        if(propertyValue == null){
            System.out.println("Resource path of \"" + property + "\" has not been found");
            System.exit(-1);
        }

        return propertyValue;
    }
}
