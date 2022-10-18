package parkingstrategies;

import exceptions.ParkingSpotNotFound;
import exceptions.UnknownVehicleType;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.*;

import static parkingspots.ParkingSpotType.*;

public class VipUserParkingStrategy implements ParkingStrategy {
    private Map<VehicleType, List<ParkingSpotType>> fittingParkingSpots;

    public
    @Override ParkingSpot getParkingSpotForVehicle(Vehicle vehicle, ParkingLotRepository parkingLotRepository) throws ParkingSpotNotFound {
        populateFittingParkingSpots();
        validateSelectedVehicleType(vehicle.getVehicleType(), fittingParkingSpots);
        return getParkingSpot(vehicle, parkingLotRepository);
    }

    private void populateFittingParkingSpots() {
        fittingParkingSpots = new LinkedHashMap<>();

        fittingParkingSpots.put(VehicleType.MOTORCYCLE, List.of(SMALL, MEDIUM, LARGE));
        fittingParkingSpots.put(VehicleType.CAR, List.of(MEDIUM, LARGE));
        fittingParkingSpots.put(VehicleType.TRUCK, List.of(LARGE));
    }

    private void validateSelectedVehicleType(VehicleType selectedVehicleType, Map<VehicleType, List<ParkingSpotType>> fittingParkingSpots) {
        if (fittingParkingSpots.get(selectedVehicleType) == null) {
            throw new UnknownVehicleType("Unknown vehicle type " + selectedVehicleType);
        }
    }

    private ParkingSpot getParkingSpot(Vehicle vehicle, ParkingLotRepository parkingLotRepository) throws ParkingSpotNotFound {
        ParkingSpotNotFound parkingSpotNotFound = null;

        List<ParkingSpotType> givenFittingParkingSpots = fittingParkingSpots.get(vehicle.getVehicleType());
        for (ParkingSpotType pst : givenFittingParkingSpots) {
            try {
                if (vehicle.isElectric()) {
                    return parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(pst);
                } else {
                    return parkingLotRepository.getEmptyParkingSpotOfType(pst);
                }
            } catch (ParkingSpotNotFound e) {
                parkingSpotNotFound = e;
            }

        }
        throw parkingSpotNotFound;
    }
}

