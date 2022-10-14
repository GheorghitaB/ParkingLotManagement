package parkingstrategies;

import exceptions.FullParkingLotException;
import parkinglots.ParkingLotDAO;
import parkingspots.ParkingSpot;
import vehicles.Vehicle;

public interface ParkingStrategy {
    ParkingSpot getParkingSpotForVehicle(Vehicle vehicle, ParkingLotDAO parkingLot) throws FullParkingLotException;
}
