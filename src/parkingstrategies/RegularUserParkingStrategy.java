package parkingstrategies;

import exceptions.ParkingSpotNotFound;
import exceptions.UnknownVehicleType;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.HashMap;
import java.util.Map;

public class RegularUserParkingStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot getParkingSpotForVehicle(Vehicle vehicle, ParkingLotRepository parkingLot) throws ParkingSpotNotFound {
        VehicleType vehicleType = vehicle.getVehicleType();

        Map<VehicleType, ParkingSpotType> map = new HashMap<>();
        map.put(VehicleType.MOTORCYCLE, ParkingSpotType.SMALL);
        map.put(VehicleType.CAR, ParkingSpotType.MEDIUM);
        map.put(VehicleType.TRUCK, ParkingSpotType.LARGE);

        if(map.get(vehicleType) == null){
            throw new UnknownVehicleType("Unknown vehicle type " + vehicleType);
        }

        if(vehicle.isElectric()){
            return parkingLot.getEmptyParkingSpotWithElectricChargerOfType(map.get(vehicleType));
        } else {
            return parkingLot.getEmptyParkingSpotOfType(map.get(vehicleType));
        }
    }
}
