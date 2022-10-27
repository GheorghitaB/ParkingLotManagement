import exceptions.ParkingSpotNotFound;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        parkingSpots.add(new SmallParkingSpot(false));
        parkingSpots.add(new MediumParkingSpot(false));
        parkingSpots.add(new MediumParkingSpot(false));
        parkingSpots.add(new LargeParkingSpot(true));

        ParkingLotRepository parkingLotRepository = new ParkingLotInMemoryRepository(parkingSpots);
        ParkingLotManager parkingLotManager = new ParkingLotManager(parkingLotRepository, ParkingStrategyFactory.getParkingStrategyInstance());

        User user = new VIPUser("John Smith");
        Vehicle vehicle = new Motorcycle("22BB", true);

        try {
            Ticket ticket = parkingLotManager.park(user, vehicle);
            System.out.println(ticket.toString());
        } catch (ParkingSpotNotFound e) {
            System.out.println("Parking lot unavailable for user type " + user.getUserType()
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