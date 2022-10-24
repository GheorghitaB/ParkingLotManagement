package parkingstrategies;

import parkinglots.ParkingLotRepository;
import parkingspots.ParkingSpot;
import vehicles.Vehicle;

import java.util.Optional;

public interface ParkingStrategy {
    Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingLotRepository parkingLot);
}
