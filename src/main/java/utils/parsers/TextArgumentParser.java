package utils.parsers;

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
    public static final String DEFAULT_COMMENT_PREFIX = "#";
    public static final String DEFAULT_SPLIT_REGEX = " ";
    public static final int UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION = -1;

    public static List<String> readLines(String resourcePath) {
        List<String> lines = new ArrayList<>();
        URL resourcePathURL = TextArgumentClassLoader.getClassLoader().getResource(resourcePath);

        if(resourcePathURL == null){
            System.out.println("Resource \"" + resourcePath + "\" has not been not found.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }

        try {
            Stream<String> stream = Files.lines(Path.of(resourcePathURL.toURI()));
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

    /**
     * @param line with or without comment prefix
     * @return <b>true</b> if the line has a comment prefix
     * or <b>false</b> if not
     * <p>The default prefix comment is: {@value DEFAULT_COMMENT_PREFIX} </p>
     */
    public static boolean notComment(String line){
        return notComment(line, DEFAULT_COMMENT_PREFIX);
    }

    public static boolean notComment(String line, String commentPrefix){
        return !StringUtils.startsWith(line, commentPrefix) && !StringUtils.isEmpty(line);
    }

    /**
     * @param line the unprepared line
     * @return the line with leading and trailing white space removed to uppercase
     */
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

    public static class TextArgumentClassLoader {
        public static ClassLoader getClassLoader(){
            return TextArgumentParser.class.getClassLoader();
        }
    }
}
