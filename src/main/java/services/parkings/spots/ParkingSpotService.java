package services.parkings.spots;

import models.parkings.spots.ParkingSpot;
import models.parkings.spots.ParkingSpotType;

import java.util.Optional;

public interface ParkingSpotService {
    Optional<ParkingSpot> findVehicleByPlateNumber(String plateNumber);
    Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerOfType(ParkingSpotType type);
    Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType type);
}
