package parkingstrategies;

import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.*;

import static parkingspots.ParkingSpotType.*;

public class VipUserParkingStrategy implements ParkingStrategy {
    private static final Map<VehicleType, List<ParkingSpotType>> fittingParkingSpots;

    static{
        fittingParkingSpots = new LinkedHashMap<>();
        fittingParkingSpots.put(VehicleType.MOTORCYCLE, List.of(SMALL, MEDIUM, LARGE));
        fittingParkingSpots.put(VehicleType.CAR, List.of(MEDIUM, LARGE));
        fittingParkingSpots.put(VehicleType.TRUCK, List.of(LARGE));
    }

    public
    @Override
    Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingLotRepository parkingLotRepository) {
        return getParkingSpotOptional(vehicle, parkingLotRepository);
    }

    private Optional<ParkingSpot> getParkingSpotOptional(Vehicle vehicle, ParkingLotRepository parkingLotRepository) {
        Optional<ParkingSpot> parkingSpotOptional = vehicle.isElectric() ? getEmptyParkingSpotWithElectricChargerForVehicleType(vehicle.getVehicleType(), parkingLotRepository)

                                                   : getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType(), parkingLotRepository);

        if(parkingSpotOptional.isEmpty() && vehicle.isElectric()){
            parkingSpotOptional = getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType(), parkingLotRepository);
        }

        return parkingSpotOptional;
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerForVehicleType(VehicleType vehicleType, ParkingLotRepository repository){
        List<ParkingSpotType> fittingParkingSpotsList = fittingParkingSpots.get(vehicleType);

        return fittingParkingSpotsList
                .stream()
                .map(repository::getEmptyParkingSpotWithElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerForVehicleType(VehicleType vehicleType, ParkingLotRepository repository){
        List<ParkingSpotType> fittingParkingSpotsList = fittingParkingSpots.get(vehicleType);

        return fittingParkingSpotsList
                .stream()
                .map(repository::getEmptyParkingSpotWithoutElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}

