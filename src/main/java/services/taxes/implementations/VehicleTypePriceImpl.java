package services.taxes.implementations;

import exceptions.VehicleTypeNotFoundException;
import models.vehicles.VehicleType;
import services.taxes.VehicleTypePrice;
import utils.Constants;

public class VehicleTypePriceImpl implements VehicleTypePrice {
    @Override
    public double getVehicleTypePrice(VehicleType vehicleType) throws VehicleTypeNotFoundException {
        switch (vehicleType){
            case MOTORCYCLE: return Constants.BASE_PRICE_FOR_MOTORCYCLE_TYPE;
            case CAR: return Constants.BASE_PRICE_FOR_CAR_TYPE;
            case TRUCK: return Constants.BASE_PRICE_FOR_TRUCK_TYPE;
            default: throw new VehicleTypeNotFoundException("Vehicle type " + vehicleType + " does not exist");
        }
    }
}
