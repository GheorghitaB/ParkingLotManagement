import exceptions.ParkingSpotNotFound;
import inits.parkingspots.ParkingSpotsInit;
import repositories.ParkingSpotRepository;
import parkinglots.ParkingLotManager;
import repositories.ParkingSpotInMemoryRepository;
import parkingspots.*;
import parkingstrategies.ParkingStrategyFactory;
import properties.AppProperty;
import tickets.Ticket;
import users.User;
import users.VIPUser;
import vehicles.Motorcycle;
import vehicles.Vehicle;

import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) {
        List<ParkingSpot> parkingSpotList = ParkingSpotsInit.getListOfParkingSpotsFromResource(AppProperty.getResourcePath("parking-spots"));
        ParkingSpotRepository parkingSpotRepository = new ParkingSpotInMemoryRepository(parkingSpotList);
        ParkingLotManager parkingLotManager = new ParkingLotManager(parkingSpotRepository, ParkingStrategyFactory.getParkingStrategyInstance());

        User user = new VIPUser("John Smith");
        Vehicle vehicle = new Motorcycle("22BB", true);

        try {
            Ticket ticket = parkingLotManager.park(user, vehicle);
            System.out.println(ticket.toString());
        } catch (ParkingSpotNotFound e) {
            System.out.println("Parking spot unavailable (not found) for user type " + user.getUserType()
                                    + " with vehicle type " + vehicle.getVehicleType());
        }


        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(vehicle.getPlateNumber());
        if(parkingSpotOptional.isPresent()){
            System.out.println(vehicle.getPlateNumber() + " is parked on parking spot id " + parkingSpotOptional.get().getId()
                    + " (" + parkingSpotOptional.get().getParkingSpotType()+")");
        } else {
            System.out.println("Parking spot has not been found.");
        }
    }
}