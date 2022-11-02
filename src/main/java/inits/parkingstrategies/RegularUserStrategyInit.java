package inits.parkingstrategies;

import inits.TextArgumentParser;
import inits.parkingspots.ParkingSpotsInit;
import parkingspots.ParkingSpotType;
import vehicles.VehicleType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegularUserStrategyInit extends TextArgumentParser {
    private static final int NUMBER_OF_ARGUMENTS = 2;

    public static Map<VehicleType, ParkingSpotType> getMapOfFittingParkingSpotsFromResource(String resourcePath, String lineSplitByString){
        Map<VehicleType, ParkingSpotType> fittingParkingSpots = new HashMap<>();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            is = ParkingSpotsInit.class.getClassLoader().getResourceAsStream(resourcePath);
            if (is == null){
                throw new FileNotFoundException("Resource \"" + resourcePath + "\" has not been found.");
            }
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            int lineNumber = 0;
            String line;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = prepareLine(line);

                if(!skipLine(line)){
                    String[] arguments = line.split(lineSplitByString);
                    validateLineArguments(lineNumber, line, arguments);
                    populateFittingParkingSpots(fittingParkingSpots, lineNumber, line, arguments);
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("The resourcePath " + resourcePath + " has not been found.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        } catch (IOException e) {
            System.out.println("Error while reading resourcePath " + resourcePath);
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        } finally {
            closeStreams(is, isr, br);
        }

        displayInConsoleFittingParkingSpots(fittingParkingSpots);
        return fittingParkingSpots;
    }

    private static void populateFittingParkingSpots(Map<VehicleType, ParkingSpotType> fittingParkingSpots, int lineNumber, String line, String[] arguments) {
        VehicleType vehicleType = VehicleType.valueOf(arguments[0]);
        ParkingSpotType parkingSpotType = ParkingSpotType.valueOf(arguments[1]);

        if(!vehicleTypeIsAlreadyInserted(fittingParkingSpots, vehicleType)){
            fittingParkingSpots.put(vehicleType, parkingSpotType);
        } else {
            System.out.println("Vehicle type \"" + arguments[0] + "\" is already inserted, therefore line "
                    + lineNumber + ": " + line + " is ignored...");
        }
    }

    private static boolean vehicleTypeIsAlreadyInserted(Map<VehicleType, ParkingSpotType> fittingParkingSpots, VehicleType vehicleType){
        return fittingParkingSpots.containsKey(vehicleType);
    }

    private static void displayInConsoleFittingParkingSpots(Map<VehicleType, ParkingSpotType> fittingParkingSpots) {
        fittingParkingSpots.forEach((vehicleType, parkingSpotType) -> System.out.println(vehicleType + " -> " + parkingSpotType));
    }

    private static void validateLineArguments(int lineNumber, String line, String[] arguments){
        try {
            validateNumberOfArguments(lineNumber, line, arguments);
            validateVehicleTypeArgument(lineNumber, line, arguments);
            validateParkingSpotTypeArgument(lineNumber, line, arguments);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }
    }

    private static void validateNumberOfArguments(int lineNumber, String line, String[] arguments){
        if(arguments.length != NUMBER_OF_ARGUMENTS){
            throw new IllegalArgumentException("Illegal number of arguments at line: " + lineNumber + ": "
                    + line + ". The number of arguments must be " + NUMBER_OF_ARGUMENTS
                    + ". It was " + arguments.length);
        }
    }

    private static void validateVehicleTypeArgument(int lineNumber, String line, String[] arguments){
        try{VehicleType.valueOf(arguments[0]);}
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Illegal arguments at line " + lineNumber + ": "  + line +
                    ". The vehicle type \"" + arguments[0] + "\" is not valid.");
        }
    }

    private static void validateParkingSpotTypeArgument(int lineNumber, String line, String[] arguments){
        try{ParkingSpotType.valueOf(arguments[1]);}
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Illegal arguments at line: " + lineNumber + ": "
                    + line + ". The parking spot type \"" + arguments[1] + "\" is not valid.");
        }
    }
}
