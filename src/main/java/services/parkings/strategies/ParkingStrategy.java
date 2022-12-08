package services.parkings.strategies;

import models.parkings.spots.ParkingSpot;
import services.parkings.spots.ParkingSpotService;
import models.vehicles.Vehicle;

import java.util.Optional;

public interface ParkingStrategy {
    Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingSpotService parkingSpotService);
}
