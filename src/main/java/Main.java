import Utils.Constants;
import exceptions.ParkingSpotNotFound;
import inits.parkingspots.ParkingSpotsInit;
import repositories.ParkingSpotService;
import parkinglots.ParkingLotManager;
import repositories.ParkingSpotInMemoryService;
import parkingspots.*;
import parkingstrategies.ParkingStrategyFactory;
import properties.AppProperty;
import writers.JsonWriter;
import tickets.Ticket;
import users.RegularUser;
import users.User;
import users.VIPUser;
import vehicles.Motorcycle;
import vehicles.Vehicle;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) {
        List<ParkingSpot> parkingSpotList = ParkingSpotsInit.getListOfParkingSpotsFromResource(AppProperty.getProperty(Constants.PARKING_SPOTS_FILEPATH_PROPERTY));
        ParkingSpotService parkingSpotService = new ParkingSpotInMemoryService(parkingSpotList);
        ParkingLotManager parkingLotManager = new ParkingLotManager(parkingSpotService, ParkingStrategyFactory.getParkingStrategyInstance());

        User user = new VIPUser("John Smith");
        Vehicle vehicle = new Motorcycle("22BB", true);

        User anotherUser = new RegularUser("NoName");
        Vehicle anotherVehicle = new Motorcycle("22BC", true);

        try {
            Ticket ticket = parkingLotManager.park(user, vehicle);
            JsonWriter jsonWriter = new JsonWriter();
            File ticketLocation = new File(System.getProperty("user.dir") + "/" + AppProperty.getProperty(Constants.TICKET_SAVING_PATH) + "/ticket_" + ticket.getID() + ".json");
            jsonWriter.write(ticketLocation, ticket);
            System.out.println("Saved: " + ticketLocation.getName());
        } catch (ParkingSpotNotFound e) {
            System.out.println("Parking spot unavailable (not found) for user type " + user.getUserType()
                                    + " with vehicle type " + vehicle.getVehicleType());
        } catch (IOException e) {
            System.out.println("Ticket could not be printed as json: " + e.getMessage());
        }

        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(vehicle.getPlateNumber());
        if(parkingSpotOptional.isPresent()){
            System.out.println(vehicle.getPlateNumber() + " is parked on parking spot id " + parkingSpotOptional.get().getId()
                    + " (" + parkingSpotOptional.get().getParkingSpotType()+")");
        } else {
            System.out.printf("Parking spot for plate number %s has not been found.\n", vehicle.getPlateNumber());
        }

        System.out.println();

        try {
            Ticket anotherTicket = parkingLotManager.park(anotherUser, anotherVehicle);
            JsonWriter jsonWriter = new JsonWriter();
            File ticketLocation = new File(System.getProperty("user.dir") + "/" + AppProperty.getProperty(Constants.TICKET_SAVING_PATH) + "/ticket_" + anotherTicket.getID() + ".json");
            jsonWriter.write(ticketLocation, anotherTicket);
            System.out.println("Saved: " + ticketLocation.getName());
        } catch (ParkingSpotNotFound e) {
            System.out.println("Parking spot unavailable (not found) for user type " + anotherUser.getUserType()
                    + " with vehicle type " + anotherVehicle.getVehicleType());
        } catch (IOException e) {
            System.out.println("Ticket could not be printed as json: " + e.getMessage());
        }

        Optional<ParkingSpot> anotherParkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(anotherVehicle.getPlateNumber());
        if(anotherParkingSpotOptional.isPresent()){
            System.out.println(anotherVehicle.getPlateNumber() + " is parked on parking spot id " + anotherParkingSpotOptional.get().getId()
                    + " (" + anotherParkingSpotOptional.get().getParkingSpotType()+")");
        } else {
            System.out.printf("Parking spot for plate number %s has not been found.\n", anotherVehicle.getPlateNumber());
        }
    }
}