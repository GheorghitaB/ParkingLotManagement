package parkinglots;

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
    public Optional<ParkingSpot> findVehicle(Vehicle vehicle){
        return parkingSpots.stream()
                .filter(parkingSpot -> !parkingSpot.isEmpty() && parkingSpot.getVehicle().getPlateNumber().equals(vehicle.getPlateNumber()))
                .findFirst();
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
