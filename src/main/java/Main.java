import exceptions.ParkingSpotNotFound;
import exceptions.VehicleNotFound;
import parkinglots.ParkingLotRepository;
import parkinglots.ParkingLotManager;
import parkinglots.ParkingLotInMemoryRepository;
import parkingspots.*;
import users.User;
import users.VIPUser;
import vehicles.Motorcycle;
import vehicles.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        parkingSpots.add(new SmallParkingSpot(false));
        parkingSpots.add(new MediumParkingSpot(false));
        parkingSpots.add(new MediumParkingSpot(false));
        parkingSpots.add(new LargeParkingSpot(true));

        ParkingLotRepository parkingLotRepository = new ParkingLotInMemoryRepository(parkingSpots);
        ParkingLotManager parkingLotManager = new ParkingLotManager(parkingLotRepository);

        User user = new VIPUser("John Smith");
        Vehicle vehicle = new Motorcycle("22BB", true);

        try {
            parkingLotManager.park(user, vehicle);

            ParkingSpot spot = parkingLotManager.findVehicle(vehicle);
            System.out.println(vehicle.getPlateNumber() + " is parked on parking spot id " + spot.getId()
                    + " (" + spot.getParkingSpotType()+")");
        } catch (VehicleNotFound e) {
            System.out.println("The vehicle has not been found");
        } catch (ParkingSpotNotFound e) {
            System.out.println("Unavailable parking spots right now");
        }
    }
}