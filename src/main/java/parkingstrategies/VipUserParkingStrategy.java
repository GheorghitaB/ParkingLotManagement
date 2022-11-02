package parkingstrategies;

import inits.parkingstrategies.VipUserStrategyInit;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.*;


public class VipUserParkingStrategy implements ParkingStrategy {
    private static final Map<VehicleType, List<ParkingSpotType>> FITTING_PARKING_SPOTS;
    private final ParkingLotRepository parkingLotRepository;

    public VipUserParkingStrategy(ParkingLotRepository parkingLotRepository){
        this.parkingLotRepository = parkingLotRepository;
    }

    static{
        FITTING_PARKING_SPOTS = VipUserStrategyInit.getParkingSpotsFitsFromResource("parkingstrategies/vip-user-strategy.init", " ");
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
                .map(parkingLotRepository::getEmptyParkingSpotWithElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<ParkingSpot> getEmptyParkingSpotWithoutElectricChargerForVehicleType(VehicleType vehicleType){
        List<ParkingSpotType> fittingParkingSpotsList = FITTING_PARKING_SPOTS.get(vehicleType);

        return fittingParkingSpotsList
                .stream()
                .map(parkingLotRepository::getEmptyParkingSpotWithoutElectricChargerOfType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}

