package parkingstrategies;

import parkingspots.ParkingSpot;
import repositories.ParkingSpotRepository;
import vehicles.Vehicle;

import java.util.Optional;

public interface ParkingStrategy {
    Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingSpotRepository parkingSpotRepository);
}
