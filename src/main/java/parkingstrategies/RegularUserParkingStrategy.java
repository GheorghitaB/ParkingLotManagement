package parkingstrategies;

import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegularUserParkingStrategy implements ParkingStrategy {
    private static final Map<VehicleType, ParkingSpotType> FITTING_PARKING_SPOTS;
    private final ParkingLotRepository parkingLotRepository;

    public RegularUserParkingStrategy(ParkingLotRepository parkingLotRepository){
        this.parkingLotRepository = parkingLotRepository;
    }

    static{
        FITTING_PARKING_SPOTS = new HashMap<>();
        FITTING_PARKING_SPOTS.put(VehicleType.MOTORCYCLE, ParkingSpotType.SMALL);
        FITTING_PARKING_SPOTS.put(VehicleType.CAR, ParkingSpotType.MEDIUM);
        FITTING_PARKING_SPOTS.put(VehicleType.TRUCK, ParkingSpotType.LARGE);
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
