package inits.parkingstrategies;

import Utils.TextArgumentParser;
import Utils.validators.ArgumentValidator;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import parkingspots.ParkingSpotType;
import vehicles.VehicleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

class VipUserStrategyInitTest {

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
    void getParkingSpotsFitsFromResource_ShouldReturnEmptyMapWhenTheFileIndicatedByResourcePathIsEmpty(){
        textArgumentParserMockedStatic.when( ()-> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of());
        Map<VehicleType, Set<ParkingSpotType>> resultMap = VipUserStrategyInit.getParkingSpotsFitsFromResource(RESOURCE_PATH_MOCK);
        assertTrue(resultMap.isEmpty());
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldReturnEmptyMapWhenTheFileIndicatedByResourcePathContainsOnlyCommentedLines(){
        String line1 = TextArgumentParser.DEFAULT_COMMENT_PREFIX + " this is line 1";
        String line2 = TextArgumentParser.DEFAULT_COMMENT_PREFIX + " this is line 2";
        List<String> lines = List.of(line1, line2);

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(lines);
        lines.forEach(line -> textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line));
        lines.forEach(line -> textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(false));

        Map<VehicleType, Set<ParkingSpotType>> resultMap = VipUserStrategyInit.getParkingSpotsFitsFromResource(RESOURCE_PATH_MOCK);
        assertTrue(resultMap.isEmpty());
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldCloseTheApplicationWithStatusMinusOneWhenNumberOfMinimumArgumentsIsNotValidated() throws Exception {
        String line = "line_without_minimum_number_of_arguments"; // minimum of arguments is 2
        String[] arguments = {"line_without_minimum_number_of_arguments"};

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfMinimumArguments(arguments, VipUserStrategyInit.NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS)).thenReturn(false);

        int status = SystemLambda.catchSystemExit(()-> VipUserStrategyInit.getParkingSpotsFitsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(TextArgumentParser.UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION, status);
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldCloseTheApplicationWithStatusMinusOneWhenParkingSpotTypeArgumentIsNotValidated() throws Exception {
        String line = "MOTORCYCLE SMALL UNKNOWN_PARKING_SPOT_TYPE";
        String[] arguments = {"MOTORCYCLE", "SMALL", "UNKNOWN_PARKING_SPOT_TYPE"};

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfMinimumArguments(arguments, VipUserStrategyInit.NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(arguments[1])).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(arguments[2])).thenReturn(false);

        int status = SystemLambda.catchSystemExit(()-> VipUserStrategyInit.getParkingSpotsFitsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(TextArgumentParser.UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION, status);
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldCloseTheApplicationWithStatusMinusOneWhenVehicleTypeArgumentIsNotValidated() throws Exception {
        String line = "UNKNOWN_VEHICLE_TYPE SMALL";
        String[] arguments = {"UNKNOWN_VEHICLE_TYPE", "SMALL"};

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfMinimumArguments(arguments, VipUserStrategyInit.NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(arguments[1])).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateVehicleTypeArgument(arguments[0])).thenReturn(false);

        int status = SystemLambda.catchSystemExit(()-> VipUserStrategyInit.getParkingSpotsFitsFromResource(RESOURCE_PATH_MOCK));
        assertEquals(TextArgumentParser.UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION, status);
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldReturnMapWithMediumAndLargeForCarAndLargeForTruckWhenTheFileIndicatedByResourcePathContainsTheseInfo(){
        String line1 = "CAR MEDIUM LARGE";
        String line2 = "TRUCK LARGE";
        List<String> lines = List.of(line1, line2);
        String[] argumentsLine1 = {"CAR", "MEDIUM", "LARGE"};
        String[] argumentsLine2 = {"TRUCK", "LARGE"};
        List<String[]> arguments = List.of(argumentsLine1, argumentsLine2);


        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(lines);
        AtomicInteger i = new AtomicInteger(0);
        lines.forEach(line -> {
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments.get(i.get()));
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments.get(i.get()))).thenReturn(arguments.get(i.get()));
            argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfMinimumArguments(arguments.get(i.get()), VipUserStrategyInit.NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS)).thenReturn(true);
            i.getAndIncrement();
        });
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(anyString())).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateVehicleTypeArgument(anyString())).thenReturn(true);

        Map<VehicleType, Set<ParkingSpotType>> resultMap = VipUserStrategyInit.getParkingSpotsFitsFromResource(RESOURCE_PATH_MOCK);
        assertEquals(2, resultMap.size());
        assertTrue(resultMap.containsKey(VehicleType.CAR));
        assertTrue(resultMap.containsKey(VehicleType.TRUCK));
        assertEquals(2, resultMap.get(VehicleType.CAR).size());
        assertEquals(1, resultMap.get(VehicleType.TRUCK).size());
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldReturnMapWithUniqueParkingSpotTypesForEachVehicleType(){
        String line = "CAR MEDIUM LARGE MEDIUM";
        String[] arguments = {"CAR", "MEDIUM", "LARGE", "MEDIUM"};

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(List.of(line));
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(arguments);
        textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(arguments)).thenReturn(arguments);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfMinimumArguments(arguments, VipUserStrategyInit.NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS)).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(anyString())).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateVehicleTypeArgument(anyString())).thenReturn(true);

        Map<VehicleType, Set<ParkingSpotType>> resultMap = VipUserStrategyInit.getParkingSpotsFitsFromResource(RESOURCE_PATH_MOCK);
        assertEquals(1, resultMap.size());
        assertEquals(2, resultMap.get(VehicleType.CAR).size());
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldReturnMapWithUniqueVehicleTypeKeys(){
        String line1 = "CAR MEDIUM LARGE";
        String line2 = "CAR LARGE";
        List<String> lines = List.of(line1, line2);
        String[] argumentsLine1 = {"CAR", "MEDIUM", "LARGE"};
        String[] argumentsLine2 = {"CAR", "LARGE"};
        Map<String, String[]> lineAndArguments = new HashMap<>();
        lineAndArguments.put(line1, argumentsLine1);
        lineAndArguments.put(line2, argumentsLine2);

        textArgumentParserMockedStatic.when( () -> TextArgumentParser.readLines(RESOURCE_PATH_MOCK)).thenReturn(lines);
        lines.forEach(line -> {
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareLine(line)).thenReturn(line);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.notComment(line)).thenReturn(true);
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.getArgumentsFromLine(line)).thenReturn(lineAndArguments.get(line));
            textArgumentParserMockedStatic.when( () -> TextArgumentParser.prepareArguments(lineAndArguments.get(line))).thenReturn(lineAndArguments.get(line));
            argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateNumberOfMinimumArguments(lineAndArguments.get(line), VipUserStrategyInit.NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS)).thenReturn(true);
        });
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateParkingSpotTypeArgument(anyString())).thenReturn(true);
        argumentValidatorMockedStatic.when( () -> ArgumentValidator.validateVehicleTypeArgument(anyString())).thenReturn(true);

        Map<VehicleType, Set<ParkingSpotType>> resultMap = VipUserStrategyInit.getParkingSpotsFitsFromResource(RESOURCE_PATH_MOCK);
        assertEquals(1, resultMap.size());
    }
}