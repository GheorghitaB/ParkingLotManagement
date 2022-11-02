import exceptions.ParkingSpotNotFound;
import inits.parkingspots.ParkingSpotsInit;
import parkinglots.ParkingLotRepository;
import parkinglots.ParkingLotManager;
import parkinglots.ParkingLotInMemoryRepository;
import parkingspots.*;
import parkingstrategies.ParkingStrategyFactory;
import tickets.Ticket;
import users.User;
import users.VIPUser;
import vehicles.Motorcycle;
import vehicles.Vehicle;

import java.util.Optional;


public class Main {
    public static void main(String[] args) {
        ParkingLotRepository parkingLotRepository = new ParkingLotInMemoryRepository(ParkingSpotsInit.getListOfParkingSpotsFromResource("parkingspots/parking-spots.init", " "));
        ParkingLotManager parkingLotManager = new ParkingLotManager(parkingLotRepository, ParkingStrategyFactory.getParkingStrategyInstance());

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