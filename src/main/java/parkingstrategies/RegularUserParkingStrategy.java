package parkingstrategies;

import exceptions.ParkingSpotNotFound;
import exceptions.UnknownVehicleType;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegularUserParkingStrategy implements ParkingStrategy {
    private Map<VehicleType, ParkingSpotType> fittingParkingSpots;

    public RegularUserParkingStrategy(){
        populateFittingParkingSpots();
    }
    @Override
    public Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingLotRepository repository) throws ParkingSpotNotFound {
        validateVehicleType(vehicle.getVehicleType(), fittingParkingSpots);

        ParkingSpotType fittingParkingSpot = fittingParkingSpots.get(vehicle.getVehicleType());

        Optional<ParkingSpot> parkingSpotOptional = vehicle.isElectric() ? getParkingSpotWithElectricCharger(repository, fittingParkingSpot)
                                                                         : getParkingSpotWithoutElectricCharger(repository, fittingParkingSpot);

        if(parkingSpotOptional.isEmpty() && vehicle.isElectric()){
            parkingSpotOptional = getParkingSpotWithoutElectricCharger(repository, fittingParkingSpot);
        }

        validateParkingSpotOptional(parkingSpotOptional);

        return parkingSpotOptional;
    }

    private static void validateParkingSpotOptional(Optional<ParkingSpot> parkingSpotOptional) throws ParkingSpotNotFound {
        if(parkingSpotOptional.isEmpty()){
            throw new ParkingSpotNotFound("Parking spot not found");
        }
    }


    private void populateFittingParkingSpots(){
        fittingParkingSpots = new HashMap<>();
        fittingParkingSpots.put(VehicleType.MOTORCYCLE, ParkingSpotType.SMALL);
        fittingParkingSpots.put(VehicleType.CAR, ParkingSpotType.MEDIUM);
        fittingParkingSpots.put(VehicleType.TRUCK, ParkingSpotType.LARGE);
    }

    private void validateVehicleType(VehicleType selectedVehicleType, Map<VehicleType, ParkingSpotType> fittingParkingSpots) {
        if (fittingParkingSpots.get(selectedVehicleType) == null) {
            throw new UnknownVehicleType("Unknown vehicle type " + selectedVehicleType);
        }
    }

    private Optional<ParkingSpot> getParkingSpotWithElectricCharger(ParkingLotRepository parkingLot, ParkingSpotType parkingSpotType){
        return parkingLot.getEmptyParkingSpotWithElectricChargerOfType(parkingSpotType);
    }
    private Optional<ParkingSpot> getParkingSpotWithoutElectricCharger(ParkingLotRepository repository, ParkingSpotType parkingSpotType){
        return repository.getEmptyParkingSpotWithoutElectricChargerOfType(parkingSpotType);
    }
}
