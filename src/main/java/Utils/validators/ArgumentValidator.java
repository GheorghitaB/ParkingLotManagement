package Utils.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parkingspots.ParkingSpotType;
import vehicles.VehicleType;

public class ArgumentValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentValidator.class);

    public static boolean validateNumberOfArguments(String[] arguments, int expectedNumberOfArguments, String errorMessage){
        return arguments.length == expectedNumberOfArguments || invalidate(errorMessage);
    }

    public static boolean validateParkingSpotTypeArgument(String parkingSpotTypeArgument, String errorMessage){
        return ParkingSpotType.containsMember(parkingSpotTypeArgument) || invalidate(errorMessage);
    }

    public static boolean validateVehicleTypeArgument(String vehicleTypeArgument, String errorMessage){
        return VehicleType.containsMember(vehicleTypeArgument) || invalidate(errorMessage);
    }

    public static boolean validateNumberOfMinimumArguments(String[] arguments, int expectedNumberOfMinimumArguments, String errorMessage){
        return arguments.length >= expectedNumberOfMinimumArguments || invalidate(errorMessage);
    }

    private static boolean invalidate(String errorMessage){
        LOGGER.error(errorMessage);
        return false;
    }
}
