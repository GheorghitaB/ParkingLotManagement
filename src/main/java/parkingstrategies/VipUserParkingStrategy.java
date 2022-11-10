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
    private final ParkingSpotRepository parkingSpotRepository;

    public VipUserParkingStrategy(ParkingSpotRepository parkingSpotRepository){
        this.parkingSpotRepository = parkingSpotRepository;
    }

    static{
        FITTING_PARKING_SPOTS = VipUserStrategyInit.getParkingSpotsFitsFromResource(AppProperty.getProperty(Constants.VIP_USER_STRATEGY_FILEPATH_PROPERTY));
    }

    public
    @Override
    Optional<ParkingSpot> getParkingSpot(Vehicle vehicle) {
        return getParkingSpotOptional(vehicle);
    }

    private Optional<ParkingSpot> getParkingSpotOptional(Vehicle vehicle) {
        Optional<ParkingSpot> parkingSpotOptional = vehicle.isElectric() ? getEmptyParkingSpotWithElectricChargerForVehicleType(vehicle.getVehicleType())

                                                   : getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType());

        if(parkingSpotOptional.isEmpty() && vehicle.isElectric()){
            parkingSpotOptional = getEmptyParkingSpotWithoutElectricChargerForVehicleType(vehicle.getVehicleType());
        }

        return parkingSpotOptional;
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithElectricChargerForVehicleType(VehicleType vehicleType){
        List<ParkingSpotType> fittingParkingSpotsList = FITTING_PARKING_SPOTS.get(vehicleType);

        return fittingParkingSpotsList
                .stream()
                .map(parkingSpotRepository::getEmptyParkingSpotWithElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerForVehicleType(VehicleType vehicleType){
        List<ParkingSpotType> fittingParkingSpotsList = FITTING_PARKING_SPOTS.get(vehicleType);

        return fittingParkingSpotsList
                .stream()
                .map(parkingSpotRepository::getEmptyParkingSpotWithoutElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}

