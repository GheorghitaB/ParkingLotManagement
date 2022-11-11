package parkingstrategies;

import Utils.Constants;
import inits.parkingstrategies.VipUserStrategyInit;
import repositories.ParkingSpotRepository;
import parkingspots.*;
import properties.AppProperty;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.*;


public class VipUserParkingStrategy implements ParkingStrategy {
    private static final Map<VehicleType, List<ParkingSpotType>> FITTING_PARKING_SPOTS;

    static{
        FITTING_PARKING_SPOTS = VipUserStrategyInit.getParkingSpotsFitsFromResource(AppProperty.getProperty(Constants.VIP_USER_STRATEGY_FILEPATH_PROPERTY));
    }

    @Override
    public Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingSpotRepository parkingSpotRepository) {
        Optional<ParkingSpot> parkingSpotOptional = vehicle.isElectric() ? getEmptyParkingSpotWithElectricChargerForVehicleType(vehicle.getVehicleType(), parkingSpotRepository)
                                                                         : getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType(), parkingSpotRepository);

        if(parkingSpotOptional.isEmpty() && vehicle.isElectric()){
            parkingSpotOptional = getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType(), parkingSpotRepository);
        }

        return parkingSpotOptional;
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerForVehicleType(VehicleType vehicleType, ParkingSpotRepository parkingSpotRepository){
        List<ParkingSpotType> fittingParkingSpotsList = FITTING_PARKING_SPOTS.get(vehicleType);

        return fittingParkingSpotsList
                .stream()
                .map(parkingSpotRepository::getEmptyParkingSpotWithElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerForVehicleType(VehicleType vehicleType, ParkingSpotRepository parkingSpotRepository){
        List<ParkingSpotType> fittingParkingSpotsList = FITTING_PARKING_SPOTS.get(vehicleType);

        return fittingParkingSpotsList
                .stream()
                .map(parkingSpotRepository::getEmptyParkingSpotWithoutElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}

