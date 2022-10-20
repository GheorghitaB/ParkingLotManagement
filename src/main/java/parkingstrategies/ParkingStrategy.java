package parkingstrategies;

import exceptions.ParkingSpotNotFound;
import parkinglots.ParkingLotRepository;
import parkingspots.ParkingSpot;
import vehicles.Vehicle;

public interface ParkingStrategy {
    ParkingSpot getParkingSpot(Vehicle vehicle, ParkingLotRepository parkingLot) throws ParkingSpotNotFound;
}
