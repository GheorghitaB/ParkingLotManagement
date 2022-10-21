package parkinglots;

import exceptions.VehicleNotFound;
import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;
import vehicles.Vehicle;

import java.util.Optional;

public interface ParkingLotRepository {
    ParkingSpot findVehicle(Vehicle vehicle) throws VehicleNotFound;
    Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerOfType(ParkingSpotType type);
    Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType type);
}
