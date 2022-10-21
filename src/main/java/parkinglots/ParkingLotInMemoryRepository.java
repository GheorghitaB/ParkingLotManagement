package parkinglots;

import exceptions.VehicleNotFound;
import parkingspots.*;
import vehicles.Vehicle;

import java.util.List;
import java.util.Optional;

public class ParkingLotInMemoryRepository implements ParkingLotRepository {
    private final List<ParkingSpot> parkingSpots;

    public ParkingLotInMemoryRepository(List<ParkingSpot> parkingSpots){
        this.parkingSpots = parkingSpots;
    }

    @Override
    public ParkingSpot findVehicle(Vehicle vehicle) throws VehicleNotFound {
        return parkingSpots.stream()
                .filter(parkingSpot -> !parkingSpot.isEmpty() && parkingSpot.getVehicle().getPlateNumber().equals(vehicle.getPlateNumber()))
                .findFirst()
                .orElseThrow(() -> new VehicleNotFound("The vehicle has not been found"));
    }

    @Override
    public Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerOfType(ParkingSpotType type) {
        return parkingSpots.stream()
                .filter(parkingSpot -> parkingSpot.getParkingSpotType().equals(type) && parkingSpot.isEmpty())
                .findFirst();
    }

    @Override
    public Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType type){
        return parkingSpots.stream()
                .filter(parkingSpot -> parkingSpot.getParkingSpotType().equals(type) && parkingSpot.isEmpty() && parkingSpot.hasElectricCharger())
                .findFirst();
    }
}
