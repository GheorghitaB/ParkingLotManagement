package parkinglots;

import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;

import java.util.Optional;

public interface ParkingLotRepository {
    Optional<ParkingSpot> findVehicleByPlateNumber(String plateNumber);
    Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerOfType(ParkingSpotType type);
    Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType type);
}
