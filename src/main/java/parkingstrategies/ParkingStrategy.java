package parkingstrategies;

import parkingspots.ParkingSpot;
import vehicles.Vehicle;

import java.util.Optional;

public interface ParkingStrategy {
    Optional<ParkingSpot> getParkingSpot(Vehicle vehicle);
}
