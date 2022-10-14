package parkingstrategies;

import exceptions.FullParkingLotException;
import exceptions.UnknownVehicleType;
import parkinglots.ParkingLotDAO;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.Map;

public class RegularUserParkingStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot getParkingSpotForVehicle(Vehicle vehicle, ParkingLotDAO parkingLot) throws FullParkingLotException {
        VehicleType vehicleType = vehicle.getVehicleType();
        Map<ParkingSpotType, Integer> parkingSpots = parkingLot.getAllParkingSpots();

        switch (vehicleType){
            case MOTORCYCLE:
                if(parkingSpots.get(ParkingSpotType.SMALL) > 0){
                    return new SmallParkingSpot();
                } else {
                    throw new FullParkingLotException("Full parking lot exception");
                }
            case CAR:
                if(parkingSpots.get(ParkingSpotType.MEDIUM) > 0){
                    return new MediumParkingSpot();
                } else {
                    throw new FullParkingLotException("Full parking lot exception");
                }
            case TRUCK:
                if(parkingSpots.get(ParkingSpotType.LARGE) > 0){
                    return new LargeParkingSpot();
                } else {
                    throw new FullParkingLotException("Full parking lot exception");
                }
            default:
                throw new UnknownVehicleType("Unknown vehicle type");
        }
    }
}
