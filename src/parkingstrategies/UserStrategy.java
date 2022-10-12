package parkingstrategies;

import exceptions.FullParkingLotException;
import parkinglots.ParkingLot;
import parkingspots.ParkingSpot;
import vehicles.Vehicle;

public interface UserStrategy {
    ParkingSpot getParkingSpotForVehicle(Vehicle vehicle, ParkingLot parkingLot) throws FullParkingLotException;
}
