package parkinglots;

import exceptions.ParkingSpotNotFound;
import exceptions.VehicleNotFound;
import parkingspots.*;
import vehicles.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

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
    public int getSizeOfEmptyParkingSpotsOfType(ParkingSpotType parkingSpotType) {
        return parkingSpots.stream()
                .filter(parkingSpot ->  parkingSpot.getParkingSpotType().equals(parkingSpotType) && parkingSpot.isEmpty())
                .collect(Collectors.toList()).size();
    }

    @Override
    public int getSizeOfEmptyParkingSpotsWithElectricChargerOfType(ParkingSpotType parkingSpotType){
        return parkingSpots.stream()
                .filter(e -> e.hasElectricCharger() && e.getParkingSpotType().equals(parkingSpotType))
                .collect(Collectors.toList())
                .size();
    }

    @Override
    public ParkingSpot getEmptyParkingSpotOfType(ParkingSpotType type) throws ParkingSpotNotFound {
        return parkingSpots.stream()
                .filter(parkingSpot -> parkingSpot.getParkingSpotType().equals(type) && parkingSpot.isEmpty())
                .findFirst()
                .orElseThrow(() -> new ParkingSpotNotFound("Parking spot has not been found"));
    }

    @Override
    public ParkingSpot getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType type) throws ParkingSpotNotFound {
        return parkingSpots.stream()
                .filter(parkingSpot -> parkingSpot.getParkingSpotType().equals(type) && parkingSpot.isEmpty() && parkingSpot.hasElectricCharger())
                .findFirst()
                .orElseThrow(() -> new ParkingSpotNotFound("Parking spot with electric charger has not been found"));
    }
}
