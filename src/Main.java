import exceptions.ParkingLotException;
import parkinglots.ParkingLotDAO;
import parkinglots.ParkingLotManager;
import parkinglots.ParkingLotHashMapImpl;
import parkingspots.ParkingSpotType;
import users.RegularUser;
import users.User;
import users.VIPUser;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Vehicle;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<ParkingSpotType, Integer> parkingSpots = new HashMap<>();
        parkingSpots.put(ParkingSpotType.SMALL, 10);
        parkingSpots.put(ParkingSpotType.MEDIUM, 10);
        parkingSpots.put(ParkingSpotType.LARGE, 10);

        ParkingLotDAO parkingLot = new ParkingLotHashMapImpl(parkingSpots);

        User user = new RegularUser("John Smith");
        Vehicle vehicle = new Motorcycle();

        User vip = new VIPUser("Vip user");
        Vehicle car = new Car();

        ParkingLotManager parkingLotManager = new ParkingLotManager(parkingLot);
        try {
            parkingLotManager.park(user, vehicle).printTicket();
            parkingLotManager.park(vip, car).printTicket();
        } catch (ParkingLotException e) {
            System.out.println(e.getMessage());
        }
    }
}