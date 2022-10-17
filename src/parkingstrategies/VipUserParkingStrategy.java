package parkingstrategies;

import exceptions.ParkingSpotNotFound;
import exceptions.UnknownVehicleType;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.*;

public class VipUserParkingStrategy implements ParkingStrategy {

    @Override
    public ParkingSpot getParkingSpotForVehicle(Vehicle vehicle, ParkingLotRepository parkingLotRepository) throws ParkingSpotNotFound {
        VehicleType vehicleType = vehicle.getVehicleType();

        Map<VehicleType, ParkingSpotType> map = new LinkedHashMap<>();
        map.put(VehicleType.MOTORCYCLE, ParkingSpotType.SMALL);
        map.put(VehicleType.CAR, ParkingSpotType.MEDIUM);
        map.put(VehicleType.TRUCK, ParkingSpotType.LARGE);

        if(map.get(vehicleType) == null){
            throw new UnknownVehicleType("Unknown vehicle type " + vehicleType);
        }

        for(int i=0; i<map.size(); i++){
            VehicleType vehicleTypeInMap = (VehicleType) map.keySet().toArray()[i];
            if(vehicleTypeInMap.equals(vehicleType)){
                for(int j=i; j<map.size(); j++){
                    ParkingSpotType pst = map.get(map.keySet().toArray()[j]);
                    if(vehicle.isElectric()){
                        if(parkingLotRepository.getSizeOfEmptyParkingSpotsWithElectricChargerOfType(pst) > 0){
                            return parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(pst);
                        }
                    } else {
                        if(parkingLotRepository.getSizeOfEmptyParkingSpotsOfType(pst) > 0){
                            return parkingLotRepository.getEmptyParkingSpotOfType(pst);
                        }
                    }
                }
                break;
            }
        }

        throw new ParkingSpotNotFound("Parking spot not found");
    }
}

