package inits.parkingstrategies;

import static Utils.TextArgumentParser.*;

import Utils.validators.ArgumentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parkingspots.ParkingSpotType;
import vehicles.VehicleType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VipUserStrategyInit {
    private static final int NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(VipUserStrategyInit.class);

    public static Map<VehicleType, List<ParkingSpotType>> getParkingSpotsFitsFromResource(String resourcePath) {
        List<String> lines = readLines(resourcePath);
        AtomicInteger lineNumber = new AtomicInteger(0);
        Map<VehicleType, List<ParkingSpotType>> parkingSpotsFits = new LinkedHashMap<>();

        lines.forEach(line -> {
            lineNumber.getAndIncrement();
            line = prepareLine(line);

            if (notComment(line)) {
                String[] arguments = getArgumentsFromLine(line);
                arguments = prepareArguments(arguments);
                validateLineArguments(lineNumber.get(), line, arguments);
                addListOfParkingSpotFitsForAVehicleType(parkingSpotsFits, lineNumber.get(), line, arguments);
            }
        });

        return parkingSpotsFits;
    }

    private static void addListOfParkingSpotFitsForAVehicleType(Map<VehicleType, List<ParkingSpotType>> parkingSpotsFits, int lineNumber, String line, String[] arguments) {
        VehicleType vehicleType = VehicleType.valueOf(arguments[0]);
        if (parkingSpotsFits.containsKey(vehicleType)) {
            LOGGER.warn("Vehicle type \"" + vehicleType + "\" is already inserted, therefore line "
                    + lineNumber + ": " + line + " is ignored.");
        } else {
            Set<ParkingSpotType> fittingParkingSpots = getSetOfFittingParkingSpotsFromLineArguments(arguments);
            parkingSpotsFits.put(vehicleType, new ArrayList<>(fittingParkingSpots));
        }
    }

    private static Set<ParkingSpotType> getSetOfFittingParkingSpotsFromLineArguments(String[] arguments) {
        Set<ParkingSpotType> parkingSpotTypes = new HashSet<>();
        for (int i = 1; i < arguments.length; i++) {
            if (parkingSpotTypes.contains(ParkingSpotType.valueOf(arguments[i]))) {
                LOGGER.warn("Parking spot type \"" + arguments[i] + "\" is duplicated, therefore it is ignored.");
            }
            parkingSpotTypes.add(ParkingSpotType.valueOf(arguments[i]));
        }
        return parkingSpotTypes;
    }

    private static void validateLineArguments(int lineNumber, String line, String[] arguments) {
        boolean legalArguments = validateNumberOfMinimumArguments(lineNumber, line, arguments)
                && validateParkingSpotTypeArguments(lineNumber, line, arguments)
                && validateVehicleTypeArgument(lineNumber, line, arguments);

        if (!legalArguments) {
            LOGGER.info("Illegal arguments. The application will be closed.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }
    }

    private static boolean validateVehicleTypeArgument(int lineNumber, String line, String[] arguments) {
        String errorMessage = "Illegal arguments at line " + lineNumber + ": " + line + ". The vehicle type \"" + arguments[0] + "\" is not valid.";
        return ArgumentValidator.validateVehicleTypeArgument(arguments[0], errorMessage);
    }

    private static boolean validateNumberOfMinimumArguments(int lineNumber, String line, String[] arguments) {
        String errorMessage = "Illegal number of arguments at line: " + lineNumber + ": "
                + line + ". Minimum number of arguments is " + NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS
                + ". It was " + arguments.length;

        return ArgumentValidator.validateNumberOfMinimumArguments(arguments, NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS, errorMessage);
    }

    private static boolean validateParkingSpotTypeArguments(int lineNumber, String line, String[] arguments) {
        for (int i = 1; i < arguments.length; i++) {
            String errorMessage = "Illegal arguments at line: " + lineNumber + ": " + line + ". The parking spot type \"" + arguments[i] + "\" is not valid.";
            if (!ArgumentValidator.validateParkingSpotTypeArgument(arguments[i], errorMessage)) {
                return false;
            }
        }
        return true;
    }
}
