package inits.parkingspots;

import utils.parsers.TextArgumentParser;
import utils.validators.ArgumentValidator;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import models.parkings.spots.inits.ParkingSpotsInit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import models.parkings.spots.ParkingSpot;
import models.parkings.spots.ParkingSpotType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ParkingSpotsInitTest {

    public static final String RESOURCE_PATH_MOCK = "mockResourcePath";
    private MockedStatic<TextArgumentParser> textArgumentParserMockedStatic;
    private MockedStatic<ArgumentValidator> argumentValidatorMockedStatic;

    @BeforeEach
    void setUp(){
        textArgumentParserMockedStatic = mockStatic(TextArgumentParser.class);
        argumentValidatorMockedStatic = mockStatic(ArgumentValidator.class);
    }

    @AfterEach
    void closeAllMockedStatic(){
        textArgumentParserMockedStatic.close();
        argumentValidatorMockedStatic.close();
    }


    @Test
    void getListOfParkingSpotsFromResource_ShouldReturnEmptyListWhenResourcePathIndicatesAnEmptyFile(){
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of());

        List<ParkingSpot> parkingSpotList = ParkingSpotsInit.getListOfParkingSpotsFromResource(RESOURCE_PATH_MOCK);

        textArgumentParserMockedStatic.verify( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK), times(1));
        assertTrue(parkingSpotList.isEmpty());
    }

    @Test
    void getListOfParkingSpotsFromResource_ShouldReturnListWithTenSmallParkingSpotsWithECAndThreeMediumParkingSpotsWithoutEC(){
        String line1 = "SMALL 10 1";
        String line2 = "MEDIUM 3 0";
        String line3 = "LARGE 2 1";
        List<String> lines = List.of(line1, line2, line3);
        String[] argumentsLine1 = {"SMALL", "10", "1"};
        String[] argumentsLine2 = {"MEDIUM", "3", "0"};
        String[] argumentsLine3 = {"LARGE", "2", "1"};
        Map<String, String[]> lineArguments = new HashMap<>();
        lineArguments.put(line1, argumentsLine1);
        lineArguments.put(line2, argumentsLine2);
        lineArguments.put(line3, argumentsLine3);

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(lines);
        lines.forEach(line -> textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line));
        lines.forEach(line -> textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true));
        lines.forEach(line -> textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(lineArguments.get(line)));
        lines.forEach(line -> textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(lineArguments.get(line))).thenReturn(lineArguments.get(line)));
        lines.forEach(line -> argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(lineArguments.get(line), ParkingSpotsInit.NUMBER_OF_ALLOWED_ARGUMENTS)).thenReturn(true));
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(anyString())).thenReturn(true);

        List<ParkingSpot> resultList = ParkingSpotsInit.getListOfParkingSpotsFromResource(RESOURCE_PATH_MOCK);
        List<ParkingSpot> smallParkingSpots = resultList.stream().filter(e -> e.getParkingSpotType().equals(ParkingSpotType.SMALL)).collect(Collectors.toList());
        List<ParkingSpot> mediumParkingSpots = resultList.stream().filter(e -> e.getParkingSpotType().equals(ParkingSpotType.MEDIUM)).collect(Collectors.toList());
        List<ParkingSpot> largeParkingSpots = resultList.stream().filter(e -> e.getParkingSpotType().equals(ParkingSpotType.LARGE)).collect(Collectors.toList());
        assertEquals(15, resultList.size());
        assertEquals(10, smallParkingSpots.size());
        assertEquals(3, mediumParkingSpots.size());
        assertEquals(2, largeParkingSpots.size());
        smallParkingSpots.forEach(e -> assertTrue(e.hasElectricCharger()));
        mediumParkingSpots.forEach(e -> assertFalse(e.hasElectricCharger()));
        largeParkingSpots.forEach(e -> assertTrue(e.hasElectricCharger()));
    }

    @Test
    void getListOfParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenTheNumberOfArgumentsIsNotValid() throws Exception {
        String line = "line with invalid number of arguments";
        String[] arguments = {"line", "with", "invalid", "number", "of", "arguments"};

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(arguments, ParkingSpotsInit.NUMBER_OF_ALLOWED_ARGUMENTS)).thenReturn(false);
        int status = SystemLambda.catchSystemExit( () -> ParkingSpotsInit.getListOfParkingSpotsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(-1, status);
    }

    @Test
    void getListOfParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenParkingSpotTypeIsNotValid() throws Exception {
        String line = "UNKNOWN_PARKING_SPOT_TYPE 10 1";
        String[] arguments = {"UNKNOWN_PARKING_SPOT_TYPE", "10", "1"};

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(arguments, ParkingSpotsInit.NUMBER_OF_ALLOWED_ARGUMENTS)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(arguments[0])).thenReturn(false);
        int status = SystemLambda.catchSystemExit( () -> ParkingSpotsInit.getListOfParkingSpotsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(-1, status);
    }

    @ParameterizedTest
    @MethodSource("getInvalidNumberOfParkingSpots")
    void getListOfParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenNumberOfParkingSpotsIsNotValid(String invalidNumberOfParkingSpots) throws Exception {
        String line = "SMALL" + TextArgumentParser.DEFAULT_SPLIT_REGEX + invalidNumberOfParkingSpots + TextArgumentParser.DEFAULT_SPLIT_REGEX + "1";
        String[] arguments = line.split(TextArgumentParser.DEFAULT_SPLIT_REGEX);

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(arguments, ParkingSpotsInit.NUMBER_OF_ALLOWED_ARGUMENTS)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(arguments[0])).thenReturn(true);
        int status = SystemLambda.catchSystemExit( () -> ParkingSpotsInit.getListOfParkingSpotsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(-1, status);
    }

    private static Stream<Arguments> getInvalidNumberOfParkingSpots(){
        return Stream.of(
                Arguments.of("-2"),
                Arguments.of("abc"),
                Arguments.of("3Ga"),
                Arguments.of("SMALL"),
                Arguments.of("CAR")
        );
    }

    @ParameterizedTest
    @MethodSource("getInvalidElectricChargerArgument")
    void getListOfParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenElectricChargerArgumentIsNotValid(String electricChargerArgument) throws Exception {
        String line = "SMALL" + TextArgumentParser.DEFAULT_SPLIT_REGEX + "10" + TextArgumentParser.DEFAULT_SPLIT_REGEX + electricChargerArgument;
        String[] arguments = line.split(TextArgumentParser.DEFAULT_SPLIT_REGEX);

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(arguments, ParkingSpotsInit.NUMBER_OF_ALLOWED_ARGUMENTS)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(arguments[0])).thenReturn(true);
        int status = SystemLambda.catchSystemExit( () -> ParkingSpotsInit.getListOfParkingSpotsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(-1, status);
    }

    private static Stream<Arguments> getInvalidElectricChargerArgument(){
        return Stream.of(
                Arguments.of("123"),
                Arguments.of("g"),
                Arguments.of("4gh"),
                Arguments.of("!= "),
                Arguments.of(" - "),
                Arguments.of("CAR")
        );
    }

}
