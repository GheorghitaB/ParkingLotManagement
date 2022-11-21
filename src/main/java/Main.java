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
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) {
        List<ParkingSpot> parkingSpotList = ParkingSpotsInit.getListOfParkingSpotsFromResource(AppProperty.getProperty(Constants.PARKING_SPOTS_FILEPATH_PROPERTY));
        ParkingSpotService parkingSpotService = new ParkingSpotInMemoryService(parkingSpotList);
        ParkingPriceCalculator parkingPriceCalculator = new ParkingPriceCalculatorImpl(new UserTypePriceImpl(), new VehicleTypePriceImpl(), new ParkingSpotTypePriceImpl(), new DiscountCalculatorImpl());
        ParkingLotManager parkingLotManager = new ParkingLotManager(parkingSpotService, ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);
        File ticketSavingLocation = new File(System.getProperty("user.dir") + "/" + AppProperty.getProperty(Constants.TICKET_SAVING_LOCATION));

        User user = new VIPUser("John Smith");
        Vehicle vehicle = new Motorcycle("22BB", true);
        int parkingDuration = 300;

        User anotherUser = new RegularUser("NoName");
        Vehicle anotherVehicle = new Motorcycle("22BC", true);

        try {
            Ticket ticket = parkingLotManager.park(parkingDuration, user, vehicle);
            new JsonWriter<Ticket>().writeOrAppendAsArray(ticketSavingLocation, ticket);
        } catch (ParkingSpotNotFound e) {
            System.out.println("Parking spot unavailable (not found) for user type " + user.getUserType()
                                    + " with vehicle type " + vehicle.getVehicleType());
        } catch (IOException e) {
            System.out.println("Ticket could not be printed as json: " + e.getMessage());
        } catch (VehicleTypeNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UserTypeNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParkingSpotTypeNotFoundException e) {
            throw new RuntimeException(e);
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
            Ticket anotherTicket = parkingLotManager.park(parkingDuration, anotherUser, anotherVehicle);
            new JsonWriter<Ticket>().writeOrAppendAsArray(ticketSavingLocation, anotherTicket);
        } catch (ParkingSpotNotFound e) {
            System.out.println("Parking spot unavailable (not found) for user type " + anotherUser.getUserType()
                    + " with vehicle type " + anotherVehicle.getVehicleType());
        } catch (IOException e) {
            System.out.println("Ticket could not be printed as json: " + e.getMessage());
        } catch (VehicleTypeNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UserTypeNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParkingSpotTypeNotFoundException e) {
            throw new RuntimeException(e);
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