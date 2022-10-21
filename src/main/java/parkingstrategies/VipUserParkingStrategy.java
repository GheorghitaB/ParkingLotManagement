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
    @Override
    Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingLotRepository parkingLotRepository) throws ParkingSpotNotFound {
        populateFittingParkingSpots();
        validateSelectedVehicleType(vehicle.getVehicleType(), fittingParkingSpots);
        return getParkingSpotOptional(vehicle, parkingLotRepository);
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

    private Optional<ParkingSpot> getParkingSpotOptional(Vehicle vehicle, ParkingLotRepository parkingLotRepository) throws ParkingSpotNotFound {
        Optional<ParkingSpot> parkingSpotOptional = vehicle.isElectric() ? getEmptyParkingSpotWithElectricChargerForVehicleType(vehicle.getVehicleType(), parkingLotRepository)

                                                   : getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType(), parkingLotRepository);

        if(parkingSpotOptional.isEmpty() && vehicle.isElectric()){
            parkingSpotOptional = getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType(), parkingLotRepository);
        }

        validateParkingSpotOptional(parkingSpotOptional);

        return parkingSpotOptional;
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerForVehicleType(VehicleType vehicleType, ParkingLotRepository repository){
        Optional<ParkingSpot> parkingSpotOptional = Optional.empty();
        List<ParkingSpotType> fittingParkingSpotsList = fittingParkingSpots.get(vehicleType);

        for(int i=0; i<fittingParkingSpotsList.size() && parkingSpotOptional.isEmpty(); i++){
            parkingSpotOptional = repository.getEmptyParkingSpotWithElectricChargerOfType(fittingParkingSpotsList.get(i));
        }

        return parkingSpotOptional;
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerForVehicleType(VehicleType vehicleType, ParkingLotRepository repository){
        Optional<ParkingSpot> parkingSpotOptional = Optional.empty();
        List<ParkingSpotType> fittingParkingSpotsList = fittingParkingSpots.get(vehicleType);

        for(int i=0; i<fittingParkingSpotsList.size() && parkingSpotOptional.isEmpty(); i++){
            parkingSpotOptional = repository.getEmptyParkingSpotWithoutElectricChargerOfType(fittingParkingSpotsList.get(i));
        }

        return parkingSpotOptional;
    }

    private void validateParkingSpotOptional(Optional<ParkingSpot> parkingSpotOptional) throws ParkingSpotNotFound {
        if(parkingSpotOptional.isEmpty()){
            throw new ParkingSpotNotFound("Parking spot not found");
        }
    }
}

