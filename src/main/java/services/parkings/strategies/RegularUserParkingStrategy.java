package services.parkings.strategies;

import utils.Constants;
import models.parkings.spots.inits.RegularUserStrategyInit;
import models.parkings.spots.ParkingSpot;
import models.parkings.spots.ParkingSpotType;
import services.parkings.spots.ParkingSpotService;
import services.properties.AppProperty;
import models.vehicles.Vehicle;
import models.vehicles.VehicleType;

import java.util.Map;
import java.util.Optional;

public class RegularUserParkingStrategy implements ParkingStrategy {
    private static final Map<VehicleType, ParkingSpotType> FITTING_PARKING_SPOTS;

    static{
        FITTING_PARKING_SPOTS = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(AppProperty.getProperty(Constants.REGULAR_USER_STRATEGY_FILEPATH_PROPERTY));
    }

    @Override
    public Optional<ParkingSpot> getParkingSpot(Vehicle vehicle, ParkingSpotService parkingSpotService){
        ParkingSpotType fittingParkingSpot = FITTING_PARKING_SPOTS.get(vehicle.getVehicleType());

        Optional<ParkingSpot> parkingSpotOptional = vehicle.isElectric() ? getParkingSpotWithElectricCharger(fittingParkingSpot, parkingSpotService)
                                                                         : getParkingSpotWithoutElectricCharger(fittingParkingSpot, parkingSpotService);

        if(parkingSpotOptional.isEmpty() && vehicle.isElectric()){
            parkingSpotOptional = getParkingSpotWithoutElectricCharger(fittingParkingSpot, parkingSpotService);
        }

        return parkingSpotOptional;
    }

    private Optional<ParkingSpot> getParkingSpotWithElectricCharger(ParkingSpotType parkingSpotType, ParkingSpotService parkingSpotService){
        return parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(parkingSpotType);
    }
    private Optional<ParkingSpot> getParkingSpotWithoutElectricCharger(ParkingSpotType parkingSpotType, ParkingSpotService parkingSpotService){
        return parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(parkingSpotType);
    }
}
