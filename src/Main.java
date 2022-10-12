import exceptions.ParkingLotException;
import parkinglots.ParkingLot;
import parkinglots.ParkingLotManager;
import parkinglots.ParkingLotMapImpl;
import users.RegularUser;
import users.User;
import users.VIPUser;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Vehicle;

public class Main {
    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLotMapImpl();

        User user = new RegularUser("John Smith");
        Vehicle vehicle = new Motorcycle();

        User vip = new VIPUser("Vip user");
        Vehicle car = new Car();

        ParkingLotManager parkingLotManager = new ParkingLotManager(parkingLot);
        try {
            parkingLotManager.park(user, vehicle).printTicket();
            parkingLotManager.park(vip, car).printTicket();
            parkingLotManager.park(vip, car).printTicket();
            //parkingLotManager.park(vip, car).printTicket();
        } catch (ParkingLotException e) {
            throw new RuntimeException(e);
        }
    }
}