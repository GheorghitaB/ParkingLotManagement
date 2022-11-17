package parkingstrategies;

import Utils.Constants;
import inits.parkingstrategies.VipUserStrategyInit;
import repositories.ParkingSpotService;
import parkingspots.*;
import properties.AppProperty;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.*;


public class VipUserParkingStrategy implements ParkingStrategy {
    private static final Map<VehicleType, Set<ParkingSpotType>> FITTING_PARKING_SPOTS;

    static{
        FITTING_PARKING_SPOTS = VipUserStrategyInit.getParkingSpotsFitsFromResource(AppProperty.getProperty(Constants.VIP_USER_STRATEGY_FILEPATH_PROPERTY));
    }

    @Override
    public Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingSpotService parkingSpotService) {
        Optional<ParkingSpot> parkingSpotOptional = vehicle.isElectric() ? getEmptyParkingSpotWithElectricChargerForVehicleType(vehicle.getVehicleType(), parkingSpotService)
                                                                         : getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType(), parkingSpotService);

        if(parkingSpotOptional.isEmpty() && vehicle.isElectric()){
            parkingSpotOptional = getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType(), parkingSpotService);
        }

        return parkingSpotOptional;
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerForVehicleType(VehicleType vehicleType, ParkingSpotService parkingSpotService){
        Set<ParkingSpotType> fittingParkingSpotsList = FITTING_PARKING_SPOTS.get(vehicleType);

        return fittingParkingSpotsList
                .stream()
                .map(parkingSpotService::getEmptyParkingSpotWithElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerForVehicleType(VehicleType vehicleType, ParkingSpotService parkingSpotService){
        Set<ParkingSpotType> fittingParkingSpotsList = FITTING_PARKING_SPOTS.get(vehicleType);

        return fittingParkingSpotsList
                .stream()
                .map(parkingSpotService::getEmptyParkingSpotWithoutElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}

