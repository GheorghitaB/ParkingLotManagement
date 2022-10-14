package parkingstrategies;

import exceptions.FullParkingLotException;
import exceptions.UnknownVehicleType;
import parkinglots.ParkingLotDAO;
import parkingspots.*;
import vehicles.Vehicle;
import vehicles.VehicleType;

public class VipUserParkingStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot getParkingSpotForVehicle(Vehicle vehicle, ParkingLotDAO parkingLot) throws FullParkingLotException {
        VehicleType vehicleType = vehicle.getVehicleType();

        switch (vehicleType){
            case MOTORCYCLE:
                if(parkingLot.getParkingSpotSizeByType(ParkingSpotType.SMALL) > 0){
                    return new SmallParkingSpot();
                } else if(parkingLot.getParkingSpotSizeByType(ParkingSpotType.MEDIUM) > 0){
                    return new MediumParkingSpot();
                } else if(parkingLot.getParkingSpotSizeByType(ParkingSpotType.LARGE) > 0){
                    return new LargeParkingSpot();
                } else
                    throw new FullParkingLotException("Full parking lot exception");
            case CAR:
                if(parkingLot.getParkingSpotSizeByType(ParkingSpotType.MEDIUM) > 0){
                    return new MediumParkingSpot();
                } else if(parkingLot.getParkingSpotSizeByType(ParkingSpotType.LARGE) > 0){
                    return new LargeParkingSpot();
                } else
                    throw new FullParkingLotException("Full parking lot exception");
            case TRUCK:
                if(parkingLot.getParkingSpotSizeByType(ParkingSpotType.LARGE) > 0){
                    return new LargeParkingSpot();
                } else
                    throw new FullParkingLotException("Full parking lot exception");
            default:
                throw new UnknownVehicleType("Unknown vehicle type: " + vehicleType);
        }
    }
}
