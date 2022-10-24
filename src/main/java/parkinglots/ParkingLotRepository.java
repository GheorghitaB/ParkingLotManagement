package parkinglots;

import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;
import vehicles.Vehicle;

import java.util.Optional;

public interface ParkingLotRepository {
    Optional<ParkingSpot> findVehicle(Vehicle vehicle);
    Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerOfType(ParkingSpotType type);
    Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType type);
}
