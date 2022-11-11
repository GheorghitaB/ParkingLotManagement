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

    static{
        FITTING_PARKING_SPOTS = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(AppProperty.getProperty(Constants.REGULAR_USER_STRATEGY_FILEPATH_PROPERTY));
    }

    @Override
    public Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingSpotRepository parkingSpotRepository){
        ParkingSpotType fittingParkingSpot = FITTING_PARKING_SPOTS.get(vehicle.getVehicleType());

        Optional<ParkingSpot> parkingSpotOptional = vehicle.isElectric() ? getParkingSpotWithElectricCharger(fittingParkingSpot, parkingSpotRepository)
                                                                         : getParkingSpotWithoutElectricCharger(fittingParkingSpot, parkingSpotRepository);

        if(parkingSpotOptional.isEmpty() && vehicle.isElectric()){
            parkingSpotOptional = getParkingSpotWithoutElectricCharger(fittingParkingSpot, parkingSpotRepository);
        }

        return parkingSpotOptional;
    }

    private Optional<ParkingSpot> getParkingSpotWithElectricCharger(ParkingSpotType parkingSpotType, ParkingSpotRepository parkingSpotRepository){
        return parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(parkingSpotType);
    }
    private Optional<ParkingSpot> getParkingSpotWithoutElectricCharger(ParkingSpotType parkingSpotType, ParkingSpotRepository parkingSpotRepository){
        return parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(parkingSpotType);
    }
}
