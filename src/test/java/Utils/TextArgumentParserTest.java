package Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TextArgumentParserTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void notComment_ShouldReturnFalseWhenTheLineIsPrefixedWithTheDefaultCommentPrefix(){
        String line = TextArgumentParser.DEFAULT_COMMENT_PREFIX + "Comment prefixed with the default comment predix";
        boolean notComment = TextArgumentParser.notComment(line);
        assertFalse(notComment);
    }

    @Test
    void notComment_ShouldReturnTrueWhenTheLineIsNotPrefixedWithACommentPrefix(){
        String line = "Comment not prefixed with a comment prefix";
        boolean notComment = TextArgumentParser.notComment(line);
        assertTrue(notComment);
    }

    @Test
    void notComment_ShouldReturnFalseWhenTheLineIsPrefixedWithAGivenCommentPrefix(){
        String givenCommentPrefix = ":";
        String line = givenCommentPrefix + "Comment prefixed with a given comment prefix";
        boolean notComment = TextArgumentParser.notComment(line, givenCommentPrefix);
        assertFalse(notComment);
    }

    @Test
    void notComment_ShouldReturnTrueWhenTheLineIsNotPrefixedWithTheGivenCommentPrefix(){
        String givenCommentPrefix = ":";
        String line = "Comment prefixed with a given comment prefix";
        boolean notComment = TextArgumentParser.notComment(line, givenCommentPrefix);
        assertTrue(notComment);
    }

    @Test
    void prepareLine_ShouldReturnTheLineWithLeadingAndTrailingWhiteSpaceRemovedToUpperCase(){
        String line = "  line to  be preparing   ";
        String expectedResult = "LINE TO  BE PREPARING";
        String actualResult = TextArgumentParser.prepareLine(line);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void prepareArguments_ShouldReturnNotEmptyArrayOfStringArgumentsWithLeadingAndTrailingWhiteSpaceRemoved(){
        String[] arguments = {"  arg1","",""," ", "  ","arg2 ", " arg3 ", "arg4"};
        String[] expectedResult = {"arg1", "arg2", "arg3", "arg4"};
        String[] actualResult = TextArgumentParser.prepareArguments(arguments);

        assertEquals(expectedResult.length, actualResult.length);
        AtomicInteger i = new AtomicInteger(0);
        Arrays.stream(actualResult).forEach(e -> {
            assertEquals(e, expectedResult[i.get()]);
            i.getAndIncrement();
        });
    }

    @ParameterizedTest
    @MethodSource("getLinesInMockFile")
    void readLines_ShouldReturnEmptyListWhenResourcePathIndicatesAnEmptyFile(String[] linesInFile) throws URISyntaxException {
        String resourcePath = "mockResourcePath";
        ClassLoader classLoader = mock(ClassLoader.class);
        URL mockURL = mock(URL.class);
        URI mockURI = mock(URI.class);
        Path mockPath = mock(Path.class);

        try(MockedStatic<TextArgumentClassLoader> classLoaderGetterMockedStatic = mockStatic(TextArgumentClassLoader.class)){
            classLoaderGetterMockedStatic.when(TextArgumentClassLoader::getClassLoader).thenReturn(classLoader);
            when(classLoader.getResource(resourcePath)).thenReturn(mockURL);
            when(mockURL.toURI()).thenReturn(mockURI);
            try(MockedStatic<Path> pathMockedStatic = mockStatic(Path.class)){
                pathMockedStatic.when(()->Path.of(mockURI)).thenReturn(mockPath);
                try(MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)){
                    filesMockedStatic.when(() -> Files.lines(mockPath)).thenReturn(Stream.of(linesInFile));
                    List<String> lines = TextArgumentParser.readLines(resourcePath);

                    classLoaderGetterMockedStatic.verify(TextArgumentClassLoader::getClassLoader, times(1));
                    verify(classLoader, times(1)).getResource(resourcePath);
                    verify(mockURL, times(1)).toURI();
                    pathMockedStatic.verify(() -> Path.of(mockURI), times(1));
                    filesMockedStatic.verify(() -> Files.lines(mockPath), times(1));
                    assertEquals(linesInFile.length, lines.size());
                }
            }
        }
    }

    private static Stream<Arguments> getLinesInMockFile(){
        return Stream.of(
                Arguments.of((Object) new String[]{}),
                Arguments.of((Object) new String[]{"#commented line"}),
                Arguments.of((Object) new String[]{"line1", "line2"})
        );
    }
}
