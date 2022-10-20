package parkinglots;

import exceptions.ParkingSpotNotFound;
import exceptions.VehicleNotFound;
import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;
import vehicles.Vehicle;

public interface ParkingLotRepository {
    ParkingSpot findVehicle(Vehicle vehicle) throws VehicleNotFound;
    int getSizeOfEmptyParkingSpotsOfType(ParkingSpotType parkingSpotType);
    int getSizeOfEmptyParkingSpotsWithElectricChargerOfType(ParkingSpotType parkingSpotType);
    ParkingSpot getEmptyParkingSpotOfType(ParkingSpotType type) throws ParkingSpotNotFound;
    ParkingSpot getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType type) throws ParkingSpotNotFound;
}
