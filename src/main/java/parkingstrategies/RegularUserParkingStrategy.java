package parkingstrategies;

import inits.parkingstrategies.RegularUserStrategyInit;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import properties.AppProperty;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.Map;
import java.util.Optional;

public class RegularUserParkingStrategy implements ParkingStrategy {
    private static final Map<VehicleType, ParkingSpotType> FITTING_PARKING_SPOTS;
    private final ParkingLotRepository parkingLotRepository;

    public RegularUserParkingStrategy(ParkingLotRepository parkingLotRepository){
        this.parkingLotRepository = parkingLotRepository;
    }

    static{
        FITTING_PARKING_SPOTS = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(AppProperty.getResourcePath("regular-user-strategy"));
    }


    @Override
    public Optional<ParkingSpot> getParkingSpot(Vehicle vehicle){
        ParkingSpotType fittingParkingSpot = FITTING_PARKING_SPOTS.get(vehicle.getVehicleType());

        Optional<ParkingSpot> parkingSpotOptional = vehicle.isElectric() ? getParkingSpotWithElectricCharger(fittingParkingSpot)
                                                                         : getParkingSpotWithoutElectricCharger(fittingParkingSpot);

        if(parkingSpotOptional.isEmpty() && vehicle.isElectric()){
            parkingSpotOptional = getParkingSpotWithoutElectricCharger(fittingParkingSpot);
        }

        return parkingSpotOptional;
    }

    private Optional<ParkingSpot> getParkingSpotWithElectricCharger(ParkingSpotType parkingSpotType){
        return parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(parkingSpotType);
    }
    private Optional<ParkingSpot> getParkingSpotWithoutElectricCharger(ParkingSpotType parkingSpotType){
        return parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(parkingSpotType);
    }
}
