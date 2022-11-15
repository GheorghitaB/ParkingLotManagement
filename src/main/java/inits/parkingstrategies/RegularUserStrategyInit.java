package inits.parkingstrategies;

import static Utils.TextArgumentParser.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parkingspots.ParkingSpotType;
import Utils.validators.ArgumentValidator;
import vehicles.VehicleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RegularUserStrategyInit {
    public static final int NUMBER_OF_ARGUMENTS = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(RegularUserStrategyInit.class);

    public static Map<VehicleType, ParkingSpotType> getMapOfFittingParkingSpotsFromResource(String resourcePath){
        List<String> lines = readLines(resourcePath);
        AtomicInteger lineNumber = new AtomicInteger(0);
        Map<VehicleType, ParkingSpotType> fittingParkingSpots = new HashMap<>();

        lines.forEach(line ->{
            lineNumber.getAndIncrement();
            line = prepareLine(line);

            if(notComment(line)){
                String[] arguments = getArgumentsFromLine(line);
                arguments = prepareArguments(arguments);
                validateLineArguments(lineNumber.get(), line, arguments);
                addFittingParkingSpot(fittingParkingSpots, lineNumber.get(), line, arguments);
            }
        });

        return fittingParkingSpots;
    }

    private static void addFittingParkingSpot(Map<VehicleType, ParkingSpotType> fittingParkingSpots, int lineNumber, String line, String[] arguments) {
        VehicleType vehicleType = VehicleType.valueOf(arguments[0]);
        ParkingSpotType parkingSpotType = ParkingSpotType.valueOf(arguments[1]);
        String warnMessage = "Vehicle type \"" + arguments[0] + "\" is already inserted, therefore line "
                + lineNumber + ": " + line + " is ignored...";

        if(!vehicleTypeIsAlreadyInserted(fittingParkingSpots, vehicleType, warnMessage)){
            fittingParkingSpots.put(vehicleType, parkingSpotType);
        }
    }

    private static boolean vehicleTypeIsAlreadyInserted(Map<VehicleType, ParkingSpotType> fittingParkingSpots, VehicleType vehicleType, String warnMessage){
        if(fittingParkingSpots.containsKey(vehicleType)){
            LOGGER.warn(warnMessage);
            return true;
        }
        return false;
    }

    private static void validateLineArguments(int lineNumber, String line, String[] arguments){
        boolean legalArguments = validateNumberOfArguments(lineNumber, line, arguments)
                    && validateVehicleTypeArgument(lineNumber, line, arguments)
                    && validateParkingSpotTypeArgument(lineNumber, line, arguments);

        if(!legalArguments){
            LOGGER.error("Illegal arguments. The application will be closed.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }
    }

    private static boolean validateNumberOfArguments(int lineNumber, String line, String[] arguments){
        String errorMessage = "Illegal number of arguments at line: " + lineNumber + ": "
                + line + ". The number of arguments must be " + NUMBER_OF_ARGUMENTS
                + ". It was " + arguments.length;

        boolean ok = ArgumentValidator.validateNumberOfArguments(arguments, NUMBER_OF_ARGUMENTS);
        if(!ok){
            LOGGER.error(errorMessage);
        }
        return ok;
    }

    private static boolean validateVehicleTypeArgument(int lineNumber, String line, String[] arguments){
        String errorMessage = "Illegal arguments at line " + lineNumber + ": "  + line +
                ". The vehicle type \"" + arguments[0] + "\" is not valid.";

        boolean ok = ArgumentValidator.validateVehicleTypeArgument(arguments[0]);
        if(!ok){
            LOGGER.error(errorMessage);
        }
        return ok;
    }

    private static boolean validateParkingSpotTypeArgument(int lineNumber, String line, String[] arguments){
        String errorMessage = "Illegal arguments at line: " + lineNumber + ": "
                + line + ". The parking spot type \"" + arguments[1] + "\" is not valid.";

        boolean ok = ArgumentValidator.validateParkingSpotTypeArgument(arguments[1]);
        if(!ok){
            LOGGER.error(errorMessage);
        }
        return ok;
    }
}
