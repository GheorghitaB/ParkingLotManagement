package inits.parkingspots;

import parkingspots.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotsInit {
    private static final int NUMBER_OF_ALLOWED_ARGUMENTS = 3;
    private static final int UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION = -1;

    public static List<ParkingSpot> getListOfParkingSpotsFromResource(String resource, String lineSplitByString){
        List<ParkingSpot> parkingSpotsList = new ArrayList<>();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            is = ParkingSpotsInit.class.getClassLoader().getResourceAsStream(resource);
            assert is != null;
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            int lineCount = 0;
            String line;

            while((line = br.readLine()) != null){
                lineCount++;
                line = prepareLine(line);
                if(!skipLine(line)){
                    String[] arguments = getArgumentsFromLine(line, lineSplitByString);
                    validateLineArguments(lineCount, line, arguments);
                    populateParkingSpotsListFromLineArguments(parkingSpotsList, arguments);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading resource " + resource);
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        } catch (NullPointerException e){
            System.out.println("The resource " + resource + " has not been found.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        } finally {
            closeStreams(br, isr, is);
        }

        showInConsoleTheListOfParkingSpots(parkingSpotsList);
        return parkingSpotsList;
    }

    private static boolean skipLine(String line){
        return isComment(line) || line.isEmpty();
    }

    private static String prepareLine(String line) {
        return line.strip().toUpperCase();
    }

    private static String[] getArgumentsFromLine(String line, String lineSplitByString) {
        return line.split(lineSplitByString);
    }

    private static void populateParkingSpotsListFromLineArguments(List<ParkingSpot> parkingSpotsList, String[] arguments) {
        String parkingSpotType = arguments[0];
        String numberOfParkingSpots = arguments[1];
        String hasElectricCharger = arguments[2];

        ParkingSpotType chosenParkingSpotType = ParkingSpotType.valueOf(parkingSpotType);
        int chosenNumberOfParkingSpots = Integer.parseInt(numberOfParkingSpots);
        boolean isWithElectricCharger = hasElectricCharger.equals("1");

        for(int i=0; i<chosenNumberOfParkingSpots; i++){
            if(chosenParkingSpotType.equals(ParkingSpotType.SMALL)){
                parkingSpotsList.add(new SmallParkingSpot(isWithElectricCharger));
            } else if(chosenParkingSpotType.equals(ParkingSpotType.MEDIUM)){
                parkingSpotsList.add(new MediumParkingSpot(isWithElectricCharger));
            } else if(chosenParkingSpotType.equals(ParkingSpotType.LARGE)){
                parkingSpotsList.add(new LargeParkingSpot(isWithElectricCharger));
            }
        }
    }

    private static void closeStreams(BufferedReader br, InputStreamReader isr, InputStream is){
        if(br != null){
            try { br.close();}
            catch (IOException e) {
                System.out.println("Buffered Reader cannot be closed");
            }
        }
        if(isr != null){
            try{isr.close();}
            catch (IOException e){System.out.println("Input Stream Reader cannot be closed");}
        }
        if(is != null){
            try{is.close();}
            catch (IOException e){System.out.println("Input Stream cannot be closed");}
        }
    }

    private static void validateLineArguments(int lineCount, String line, String[] arguments) {
        try{
            validateNumberOfArguments(lineCount, line, arguments);
            validateParkingSpotTypeArgument(lineCount, line, arguments[0]);
            validateNumberOfParkingSpotsArgument(lineCount, line, arguments[1]);
            validateElectricChargerIsBooleanArgument(lineCount, line, arguments[2]);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }
    }

    private static void validateElectricChargerIsBooleanArgument(int lineNumber, String line, String hasElectricCharger) {
        if(!hasElectricCharger.equals("0") && !hasElectricCharger.equals("1")){
            throw new IllegalArgumentException("Illegal argument at line " + lineNumber + ": " + line +
                    ". It is allowed to be 0 or 1 (without/with electric charger). It is " + hasElectricCharger);
        }
    }

    private static void validateNumberOfParkingSpotsArgument(int lineNumber, String line, String numberOfParkingSpots) {
        if(!numberOfParkingSpots.matches("[0-9]+")){
            throw new IllegalArgumentException("Illegal argument at line " + lineNumber + ": " + line +
                    ". The number of parking spots should be a positive number. It is " + numberOfParkingSpots);
        }
    }

    private static void validateNumberOfArguments(int lineNumber, String line, String[] arguments) {
        if(arguments.length != NUMBER_OF_ALLOWED_ARGUMENTS){
            throw new IllegalArgumentException("Illegal number of arguments at line " + lineNumber + ": "  + line +
                    ". Allowed: " + NUMBER_OF_ALLOWED_ARGUMENTS + ", was " + arguments.length + ". Maybe the split string is not well defined.");
        }
    }

    private static void validateParkingSpotTypeArgument(int lineNumber, String line, String parkingSpotType){
        try{ParkingSpotType.valueOf(parkingSpotType);}
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Illegal arguments at line " + lineNumber + ": "  + line +
                    ". The parking spot type \"" + parkingSpotType + "\" is not valid.");
        }
    }

    private static boolean isComment(String line){
        return line.startsWith("#");
    }

    private static void showInConsoleTheListOfParkingSpots(List<ParkingSpot> parkingSpotsList){
        for(ParkingSpot ps : parkingSpotsList){
            System.out.println(ps.getParkingSpotType() + " (electric charger: " + ps.hasElectricCharger() + ")");
        }
    }
}
