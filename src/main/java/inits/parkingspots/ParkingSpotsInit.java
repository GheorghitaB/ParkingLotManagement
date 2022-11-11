package inits.parkingspots;

import static Utils.TextArgumentParser.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parkingspots.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingSpotsInit {
    private static final int NUMBER_OF_ALLOWED_ARGUMENTS = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingSpotsInit.class);

    public static List<ParkingSpot> getListOfParkingSpotsFromResource(String resourcePath){
        List<String> lines = readLines(resourcePath);
        List<ParkingSpot> parkingSpotsList = new ArrayList<>();
        AtomicInteger lineCount = new AtomicInteger(0);

        lines.forEach(line -> {
            lineCount.getAndIncrement();
            line = prepareLine(line);

            if(notComment(line)){
                String[] arguments = getArgumentsFromLine(line);
                arguments = prepareArguments(arguments);
                validateLineArguments(lineCount.get(), line, arguments);
                addParkingSpot(parkingSpotsList, arguments);
            }
        });

        return parkingSpotsList;
    }

    private static void addParkingSpot(List<ParkingSpot> parkingSpotsList, String[] arguments) {
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

    private static void validateLineArguments(int lineCount, String line, String[] arguments) {
        boolean legalArguments = validateNumberOfArguments(lineCount, line, arguments)
                                && validateParkingSpotTypeArgument(lineCount, line, arguments[0])
                                && validateNumberOfParkingSpotsArgument(lineCount, line, arguments[1])
                                && validateElectricChargerIsBooleanArgument(lineCount, line, arguments[2]);

        if(!legalArguments){
            LOGGER.info("Illegal arguments. The application will be closed.");
            System.exit(UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION);
        }
    }

    private static boolean validateElectricChargerIsBooleanArgument(int lineNumber, String line, String hasElectricCharger) {
        if(hasElectricCharger.equals("0") || hasElectricCharger.equals("1")){
            return true;
        } else {
            LOGGER.error("Illegal argument at line " + lineNumber + ": " + line +
                    ". It is allowed to be 0 or 1 (without/with electric charger). It is " + hasElectricCharger);
            return false;
        }
    }

    private static boolean validateNumberOfParkingSpotsArgument(int lineNumber, String line, String numberOfParkingSpots) {
        if(numberOfParkingSpots.matches("[0-9]+")){
            return true;
        } else{
            LOGGER.error("Illegal argument at line " + lineNumber + ": " + line +
                    ". The number of parking spots should be a positive number. It is " + numberOfParkingSpots);
            return false;
        }
    }

    private static boolean validateNumberOfArguments(int lineNumber, String line, String[] arguments) {
        if(arguments.length == NUMBER_OF_ALLOWED_ARGUMENTS){
            return true;
        } else {
            LOGGER.error("Illegal number of arguments at line " + lineNumber + ": "  + line +". Allowed: "
                    + NUMBER_OF_ALLOWED_ARGUMENTS + ", was " + arguments.length + ". Maybe the split string is not well defined.");
            return false;
        }
    }

    private static boolean validateParkingSpotTypeArgument(int lineNumber, String line, String parkingSpotType){
        if(ParkingSpotType.containsMember(parkingSpotType)){
            return true;
        } else {
            LOGGER.error("Illegal arguments at line " + lineNumber + ": "  + line + ". The parking spot type \""
                    + parkingSpotType + "\" is not valid.");
            return false;
        }
    }
}
