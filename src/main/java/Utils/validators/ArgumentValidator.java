package Utils.validators;

import parkingspots.ParkingSpotType;
import vehicles.VehicleType;

public class ArgumentValidator {
    public static boolean validateNumberOfArguments(String[] arguments, int expectedNumberOfArguments){
        return arguments.length == expectedNumberOfArguments;
    }

    public static boolean validateParkingSpotTypeArgument(String parkingSpotTypeArgument){
        return ParkingSpotType.containsMember(parkingSpotTypeArgument);
    }

    public static boolean validateVehicleTypeArgument(String vehicleTypeArgument){
        return VehicleType.containsMember(vehicleTypeArgument);
    }

    public static boolean validateNumberOfMinimumArguments(String[] arguments, int expectedNumberOfMinimumArguments){
        return arguments.length >= expectedNumberOfMinimumArguments;
    }
}
