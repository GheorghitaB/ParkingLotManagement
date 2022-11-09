package Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextArgumentParser {
    private static final String DEFAULT_COMMENT_REGEX = "#";
    public static final String DEFAULT_SPLIT_REGEX = " ";
    public static final int UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION = -1;

    public static List<String> readLines(String resourcePath) {
        List<String> lines = new ArrayList<>();
        URL resourcePathURL = TextArgumentParser.class.getClassLoader().getResource(resourcePath);

        if(resourcePathURL == null){
            System.out.println("Resource \"" + resourcePath + "\" has not been not found.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }

        try (Stream<String> stream = Files.lines(Path.of(resourcePathURL.toURI()))) {
            lines = stream.collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error reading from resource \"" + resourcePath + "\".");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        } catch (URISyntaxException e) {
            System.out.println("Resource + \"" + resourcePath + "\" could not be read.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }
        return lines;
    }

    public static boolean isComment(String line, String commentRegex){
        return StringUtils.startsWith(line, commentRegex);
    }

    public static boolean isEmpty(String line){
        return StringUtils.isEmpty(line);
    }

    public static boolean notComment(String line){
        return notComment(line, DEFAULT_COMMENT_REGEX);
    }

    public static boolean notComment(String line, String commentRegex){
        return !isComment(line, commentRegex) && !isEmpty(line);
    }

    public static String prepareLine(String line) {
        return line.strip().toUpperCase();
    }

    public static String[] getArgumentsFromLine(String line) {
        return getArgumentsFromLine(line, DEFAULT_SPLIT_REGEX);
    }

    public static String[] getArgumentsFromLine(String line, String splitRegex) {
        return line.split(splitRegex);
    }

    public static String[] prepareArguments(String[] arguments) {
        return Arrays.stream(arguments)
                .map(String::strip)
                .filter(arg -> !arg.isEmpty())
                .toArray(String[]::new);
    }
}
