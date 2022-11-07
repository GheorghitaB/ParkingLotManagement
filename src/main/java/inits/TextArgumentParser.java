package inits;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TextArgumentParser {
    private static final String COMMENT_SYMBOL = "#";
    protected static final String lineSplitByString = " ";
    protected static final int UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION = -1;

    protected static List<String> readLines(String resourcePath) {
        List<String> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Path.of(Objects.requireNonNull(TextArgumentParser.class.getClassLoader().getResource(resourcePath)).toURI()))) {
            lines = stream.collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error reading from resource \"" + resourcePath + "\".");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        } catch (URISyntaxException e) {
            System.out.println("Resource + \"" + resourcePath + "\" could not be read.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        } catch (NullPointerException e){
            System.out.println("Resource \"" + resourcePath + "\" has not been not found.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }
        return lines;
    }

    protected static boolean isComment(String line){
        return line.startsWith(COMMENT_SYMBOL);
    }

    protected static boolean notSkipLine(String line){
        return !isComment(line) && !line.isEmpty();
    }

    protected static String prepareLine(String line) {
        return line.strip().toUpperCase();
    }

    protected static String[] getArgumentsFromLine(String line) {
        return line.split(lineSplitByString);
    }
}
