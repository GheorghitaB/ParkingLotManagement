package parkingstrategies;

import Utils.Constants;
import inits.parkingstrategies.RegularUserStrategyInit;
import repositories.ParkingSpotRepository;
import parkingspots.*;
import properties.AppProperty;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.Map;
import java.util.Optional;

public class RegularUserParkingStrategy implements ParkingStrategy {
    private static final Map<VehicleType, ParkingSpotType> FITTING_PARKING_SPOTS;
    private final ParkingSpotRepository parkingSpotRepository;

    public RegularUserParkingStrategy(ParkingSpotRepository parkingSpotRepository){
        this.parkingSpotRepository = parkingSpotRepository;
    }

    static{
        FITTING_PARKING_SPOTS = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(AppProperty.getProperty(Constants.REGULAR_USER_STRATEGY_FILEPATH_PROPERTY));
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
        return parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(parkingSpotType);
    }
    private Optional<ParkingSpot> getParkingSpotWithoutElectricCharger(ParkingSpotType parkingSpotType){
        return parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(parkingSpotType);
    }
}
