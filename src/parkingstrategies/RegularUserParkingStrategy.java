package parkingstrategies;

import exceptions.ParkingSpotNotFound;
import exceptions.UnknownVehicleType;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.HashMap;
import java.util.Map;

public class RegularUserParkingStrategy implements ParkingStrategy {
    private Map<VehicleType, ParkingSpotType> fittingParkingSpots;
    @Override
    public ParkingSpot getParkingSpotForVehicle(Vehicle vehicle, ParkingLotRepository parkingLot) throws ParkingSpotNotFound {
        populateFittingParkingSpots();
        validateSelectedVehicleType(vehicle.getVehicleType(), fittingParkingSpots);

        if(vehicle.isElectric()){
            return parkingLot.getEmptyParkingSpotWithElectricChargerOfType(fittingParkingSpots.get(vehicle.getVehicleType()));
        } else {
            return parkingLot.getEmptyParkingSpotOfType(fittingParkingSpots.get(vehicle.getVehicleType()));
        }
    }
    private void validateSelectedVehicleType(VehicleType selectedVehicleType, Map<VehicleType, ParkingSpotType> fittingParkingSpots) {
        if (fittingParkingSpots.get(selectedVehicleType) == null) {
            throw new UnknownVehicleType("Unknown vehicle type " + selectedVehicleType);
        }
    }

    private void populateFittingParkingSpots(){
        fittingParkingSpots = new HashMap<>();
        fittingParkingSpots.put(VehicleType.MOTORCYCLE, ParkingSpotType.SMALL);
        fittingParkingSpots.put(VehicleType.CAR, ParkingSpotType.MEDIUM);
        fittingParkingSpots.put(VehicleType.TRUCK, ParkingSpotType.LARGE);
    }
}
