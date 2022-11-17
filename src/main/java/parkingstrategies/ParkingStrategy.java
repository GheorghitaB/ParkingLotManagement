package parkingstrategies;

import parkingspots.ParkingSpot;
import repositories.ParkingSpotService;
import vehicles.Vehicle;

import java.util.Optional;

public interface ParkingStrategy {
    Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingSpotService parkingSpotService);
}
