package inits.parkingstrategies;

import Utils.TextArgumentParser;
import Utils.validators.ArgumentValidator;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import parkingspots.ParkingSpotType;
import vehicles.VehicleType;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;


class RegularUserStrategyInitTest {

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
    void getMapOfFittingParkingSpotsFromResource_ShouldReturnMapWithSmallForMotorcycleAndMediumForCarAndLargeForTruckWhenThatIsReadFromFile(){
        String resourcePath = "mockResourcePath";
        // assume that REGEX SPLIT for each line is the default REGEX SPLIT: " "
        String line1 = "MOTORCYCLE SMALL";
        String line2 = "CAR MEDIUM";
        String line3 = "TRUCK LARGE";
        List<String> givenLines = List.of(line1, line2, line3);

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(resourcePath)).thenReturn(givenLines);

        givenLines.forEach(line -> {
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(line.split(TextArgumentParser.DEFAULT_SPLIT_REGEX));
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(line.split(TextArgumentParser.DEFAULT_SPLIT_REGEX))).thenReturn(line.split(TextArgumentParser.DEFAULT_SPLIT_REGEX));
            argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(line.split(TextArgumentParser.DEFAULT_SPLIT_REGEX), RegularUserStrategyInit.NUMBER_OF_ARGUMENTS)).thenReturn(true);
            argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateVehicleTypeArgument(line.split(TextArgumentParser.DEFAULT_SPLIT_REGEX)[0])).thenReturn(true);
            argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(line.split(TextArgumentParser.DEFAULT_SPLIT_REGEX)[1])).thenReturn(true);
        });

        Map<VehicleType, ParkingSpotType> fittingParkingSpots = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(resourcePath);
        assertEquals(3, fittingParkingSpots.size());
        assertEquals(ParkingSpotType.SMALL, fittingParkingSpots.get(VehicleType.MOTORCYCLE));
        assertEquals(ParkingSpotType.MEDIUM, fittingParkingSpots.get(VehicleType.CAR));
        assertEquals(ParkingSpotType.LARGE, fittingParkingSpots.get(VehicleType.TRUCK));
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldReturnEmptyMapWhenTheFileIndicatedByResourcePathIsEmpty(){
        String resourcePath = "mockResourcePath";
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(resourcePath)).thenReturn(List.of());
        Map<VehicleType, ParkingSpotType> fittingParkingSpots = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(resourcePath);
        assertTrue(fittingParkingSpots.isEmpty());
        textArgumentParserMockedStatic.verify(()->TextArgumentParser.readLines(resourcePath), times(1));
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldReturnEmptyMapWhenThereAreOnlyCommentedLines(){
        String resourcePath = "mockResourcePath";
        String commentedLine1 = TextArgumentParser.DEFAULT_COMMENT_PREFIX + "commented line";
        String commentedLine2 = TextArgumentParser.DEFAULT_COMMENT_PREFIX + "another commented line";
        List<String> lines = List.of(commentedLine1, commentedLine2);

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(resourcePath)).thenReturn(lines);
        lines.forEach(line -> textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(false));

        Map<VehicleType, ParkingSpotType> fittingParkingSpots = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(resourcePath);
        assertTrue(fittingParkingSpots.isEmpty());
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldCloseApplicationWithMinusOneStatusWhenThereIsNotACorrectNumberOfArguments() throws Exception {
        String line = "line with invalid number of arguments";
        String[] arguments = {"line", "with", "invalid", "number", "of", "arguments"};
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(arguments, RegularUserStrategyInit.NUMBER_OF_ARGUMENTS)).thenReturn(false);
        int status = SystemLambda.catchSystemExit(()-> RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(TextArgumentParser.UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION, status);
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldCloseApplicationWithMinusOneStatusCodeWhenThereIsAnUnknownVehicleType() throws Exception {
        String vehicleType = "UNKNOWN_VEHICLE_TYPE";
        String parkingSpotType = "SMALL";
        String line = vehicleType + " " + parkingSpotType;
        String[] arguments = {vehicleType, parkingSpotType};
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(arguments, RegularUserStrategyInit.NUMBER_OF_ARGUMENTS)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateVehicleTypeArgument(vehicleType)).thenReturn(false);
        int status = SystemLambda.catchSystemExit(()-> RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(TextArgumentParser.UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION, status);
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldCloseApplicationWithMinusOneStatusCodeWhenThereIsAnUnknownParkingSpotType() throws Exception {
        String vehicleType = "MOTORCYCLE";
        String parkingSpotType = "UNKNOWN_PARKING_SPOT_TYPE";
        String line = vehicleType + " " + parkingSpotType;
        String[] arguments = {vehicleType, parkingSpotType};
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(arguments, RegularUserStrategyInit.NUMBER_OF_ARGUMENTS)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateVehicleTypeArgument(vehicleType)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(parkingSpotType)).thenReturn(false);
        int status = SystemLambda.catchSystemExit(()-> RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(TextArgumentParser.UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION, status);
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldReturnMapOfSizeOneWhenThereAreTwoLinesSpecifyingTheSameVehicleType() {
        String vehicleType = "MOTORCYCLE";
        String parkingSpotType = "SMALL";
        String line1 = vehicleType + TextArgumentParser.DEFAULT_SPLIT_REGEX + parkingSpotType;
        String line2 = vehicleType + TextArgumentParser.DEFAULT_SPLIT_REGEX + parkingSpotType;
        List<String> lines = List.of(line1, line2);
        String[] arguments = {"MOTORCYCLE", "SMALL"};

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(lines);
        lines.forEach(line -> {
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
            argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(arguments, RegularUserStrategyInit.NUMBER_OF_ARGUMENTS)).thenReturn(true);
        });
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateVehicleTypeArgument(vehicleType)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(parkingSpotType)).thenReturn(true);

        Map<VehicleType, ParkingSpotType> result = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(RESOURCE_PATH_MOCK);
        assertEquals(1, result.size());
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldReturnMapOfSizeOneWhenThereAreTwoLinesAndOneLineIsCommented(){
        String line1 = "MOTORCYCLE SMALL";
        String line2 = TextArgumentParser.DEFAULT_COMMENT_PREFIX + "CAR MEDIUM";
        List<String> lines = List.of(line1, line2);
        String[] argumentsLine1 = line1.split(TextArgumentParser.DEFAULT_SPLIT_REGEX);

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(lines);

        lines.forEach(line -> textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line1)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line2)).thenReturn(false);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line1)).thenReturn(argumentsLine1);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(argumentsLine1)).thenReturn(argumentsLine1);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfArguments(argumentsLine1, RegularUserStrategyInit.NUMBER_OF_ARGUMENTS)).thenReturn(true);

        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateVehicleTypeArgument(VehicleType.MOTORCYCLE.name())).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(ParkingSpotType.SMALL.name())).thenReturn(true);

        Map<VehicleType, ParkingSpotType> result = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(RESOURCE_PATH_MOCK);
        assertEquals(1, result.size());
    }
}