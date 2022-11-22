import exceptions.ParkingSpotTypeNotFoundException;
import exceptions.UserTypeNotFoundException;
import exceptions.VehicleTypeNotFoundException;
import services.taxes.ParkingPriceCalculator;
import services.taxes.implementations.*;
import utils.Constants;
import exceptions.ParkingSpotNotFound;
import models.parkings.spots.inits.ParkingSpotsInit;
import models.parkings.spots.ParkingSpot;
import services.parkings.spots.ParkingSpotService;
import services.parkings.lots.ParkingLotManager;
import services.parkings.spots.ParkingSpotInMemoryService;
import services.parkings.strategies.ParkingStrategyFactory;
import services.properties.AppProperty;
import services.writers.JsonWriter;
import models.tickets.Ticket;
import models.users.RegularUser;
import models.users.User;
import models.users.VIPUser;
import models.vehicles.Motorcycle;
import models.vehicles.Vehicle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Main {

    private static ParkingLotManager parkingLotManager;

    public static void main(String[] args) {
        List<ParkingSpot> parkingSpotList = ParkingSpotsInit.getListOfParkingSpotsFromResource(AppProperty.getProperty(Constants.PARKING_SPOTS_FILEPATH_PROPERTY));
        ParkingSpotService parkingSpotService = new ParkingSpotInMemoryService(parkingSpotList);
        ParkingPriceCalculator parkingPriceCalculator = new ParkingPriceCalculatorImpl(new UserTypePriceImpl(), new VehicleTypePriceImpl(), new ParkingSpotTypePriceImpl(), new DiscountCalculatorImpl());
        parkingLotManager = new ParkingLotManager(parkingSpotService, ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);

        File ticketSavingLocation = new File(System.getProperty("user.dir") + "/" + AppProperty.getProperty(Constants.TICKET_SAVING_LOCATION));
        List<Ticket> tickets = new ArrayList<>();

        try {
            Ticket ticket1 = park(new VIPUser("John Smith"), new Motorcycle("22BB", true), 300);
            Ticket ticket2 = park(new RegularUser("NoName"), new Motorcycle("22BC", true), 250);

            tickets.add(ticket1);
            tickets.add(ticket2);

            displayWhereVehicleIsParked("22BB");
            displayWhereVehicleIsParked("22BC");

            writeTickets(tickets, ticketSavingLocation);

        } catch (ParkingSpotNotFound e) {
            System.out.println("There are not available parking spots right now. (" + e.getMessage() + ")");
        } catch (VehicleTypeNotFoundException e) {
            System.out.println("Vehicle type has not been found. (" + e.getMessage() + ")");
        } catch (UserTypeNotFoundException e) {
            System.out.println("User type has not been found. (" + e.getMessage() + ")");
        } catch (ParkingSpotTypeNotFoundException e) {
            System.out.println("Parking spot type has not been found. (" + e.getMessage() + ")");
        }
    }

    private static void displayWhereVehicleIsParked(String plateNumber){
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(plateNumber);
        if(parkingSpotOptional.isEmpty()){
            System.out.println("The vehicle with plate number: " + plateNumber + " is not parked.");
        } else {
            System.out.println("The vehicle with plate number: " + plateNumber + " is parked on parking spot "
                                    + parkingSpotOptional.get());
        }
    }

    private static Ticket park(User user, Vehicle vehicle, int parkDurationInMinutes)
            throws VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException, ParkingSpotNotFound {
        return parkingLotManager.park(parkDurationInMinutes, user, vehicle);
    }

    private static void writeTickets(List<Ticket> tickets, File ticketSavingLocation){
        JsonWriter<Ticket> jsonWriter = new JsonWriter<>();
        try {
            jsonWriter.write(ticketSavingLocation, tickets);
        } catch (IOException e) {
            System.out.println("The tickets could not be written");
        }
    }
}