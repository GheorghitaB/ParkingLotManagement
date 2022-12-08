import exceptions.*;
import services.api.prices.PriceService;
import services.api.prices.PriceServiceImpl;
import utils.Constants;
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
        PriceService priceService = new PriceServiceImpl();
        parkingLotManager = new ParkingLotManager(parkingSpotService, ParkingStrategyFactory.getParkingStrategyInstance(), priceService);

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
        } catch (PriceException e) {
            System.out.println("Parking unsuccessful. " + "(" + e.getMessage() + ")");
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
            throws ParkingSpotNotFound, PriceException {
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