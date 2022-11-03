package inits.parkingstrategies;

import inits.TextArgumentParser;
import inits.parkingspots.ParkingSpotsInit;
import parkingspots.ParkingSpotType;
import vehicles.VehicleType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VipUserStrategyInit extends TextArgumentParser {
    private static final int NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS = 1;

    public static Map<VehicleType, List<ParkingSpotType>> getParkingSpotsFitsFromResource(String resourcePath){
        Map<VehicleType, List<ParkingSpotType>> parkingSpotsFits = new LinkedHashMap<>();
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

                if(skipLine(line)){continue;}

                String[] arguments = line.split(lineSplitByString);
                validateLineArguments(lineNumber, line, arguments);
                populateParkingSpotsFits(parkingSpotsFits, lineNumber, line, arguments);
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
        displayInConsoleMapOfParkingSpotsFits(parkingSpotsFits);
        return parkingSpotsFits;
    }

    private static void populateParkingSpotsFits(Map<VehicleType, List<ParkingSpotType>> parkingSpotsFits, int lineNumber, String line, String[] arguments) {
        if(!vehicleTypeIsAlreadyInserted(parkingSpotsFits, arguments[0])){
            VehicleType chosenVehicleType = VehicleType.valueOf(arguments[0]);
            List<ParkingSpotType> fittingParkingSpots = getFittingParkingSpotsFromLineArguments(arguments);
            parkingSpotsFits.put(chosenVehicleType, fittingParkingSpots);
        } else {
            System.out.println("Vehicle type \"" + arguments[0] + "\" is already inserted, therefore line "
                    + lineNumber + ": " + line + " is ignored...");
        }
    }

    private static boolean vehicleTypeIsAlreadyInserted(Map<VehicleType, List<ParkingSpotType>> parkingSpotsFits, String parkingSpotType) {
        return parkingSpotsFits.containsKey(VehicleType.valueOf(parkingSpotType));
    }

    private static List<ParkingSpotType> getFittingParkingSpotsFromLineArguments(String[] arguments) {
        List<ParkingSpotType> parkingSpotTypes = new ArrayList<>();
        for(int i=1; i<arguments.length; i++){
            parkingSpotTypes.add(ParkingSpotType.valueOf(arguments[i]));
        }
        return parkingSpotTypes;
    }

    private static void validateLineArguments(int lineNumber, String line, String[] arguments){
        try {
            validateNumberOfArguments(lineNumber, line, arguments);
            validateParkingSpotTypeArguments(lineNumber, line, arguments);
            checkForDuplicateParkingSpotTypes(lineNumber, line, arguments);
            validateVehicleTypeArgument(lineNumber, line, arguments);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }
    }

    private static void checkForDuplicateParkingSpotTypes(int lineNumber, String line, String[] arguments) {
    Set<String> parkingSpotSet = new HashSet<>();
        for(int i=1; i<arguments.length; i++){
            if(parkingSpotSet.contains(arguments[i])){
                throw new IllegalArgumentException("Illegal arguments at line " + lineNumber
                                                    + ": " + line + ". Parking spot type \""
                                                    + arguments[i] + "\" is duplicated");
            } else {
                parkingSpotSet.add(arguments[i]);
            }
        }
    }

    private static void validateVehicleTypeArgument(int lineNumber, String line, String[] arguments) {
        try{VehicleType.valueOf(arguments[0]);}
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Illegal arguments at line " + lineNumber + ": "  + line +
                    ". The vehicle type \"" + arguments[0] + "\" is not valid.");
        }
    }

    private static void validateNumberOfArguments(int lineNumber, String line, String[] arguments){
        if(arguments.length < NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS){
            throw new IllegalArgumentException("Illegal number of arguments at line: " + lineNumber + ": "
                    + line + ". Minimum number of arguments is " + NUMBER_OF_MINIMUM_ALLOWED_ARGUMENTS
                    + ". It was " + arguments.length);
        }
    }

    private static void validateParkingSpotTypeArguments(int lineNumber, String line, String[] arguments) {
        for(int i=1; i<arguments.length; i++){
            try{
                ParkingSpotType.valueOf(arguments[i]);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Illegal arguments at line: " + lineNumber + ": "
                        + line + ". The parking spot type \"" + arguments[i] + "\" is not valid.");
            }
        }
    }

    private static void displayInConsoleMapOfParkingSpotsFits(Map<VehicleType, List<ParkingSpotType>> parkingSpotsFits){
        for(VehicleType key : parkingSpotsFits.keySet()){
            System.out.println("VehicleType: " + key);
            for(ParkingSpotType pst : parkingSpotsFits.get(key)){
                System.out.println("    -> ParkingSpotType: " + pst.name());
            }
        }
    }
}
