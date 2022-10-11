import parkinglots.ParkingLotManager;
import parkingspots.MediumParkingSpot;
import parkingspots.ParkingSpot;
import parkingspots.SmallParkingSpot;
import users.User;
import users.VIPUser;
import vehicles.Motorcycle;
import vehicles.Vehicle;

public class Main {
    public static void main(String[] args) {
        User user = new VIPUser("John Smith");
        Vehicle vehicle = new Motorcycle();
        ParkingSpot mediumPS = new MediumParkingSpot();
        ParkingSpot smallPS = new SmallParkingSpot();

        ParkingLotManager parkingLotManager = new ParkingLotManager();
        parkingLotManager.acceptForParking(user, vehicle, smallPS);
        parkingLotManager.acceptForParking(user, vehicle, mediumPS);
    }
}