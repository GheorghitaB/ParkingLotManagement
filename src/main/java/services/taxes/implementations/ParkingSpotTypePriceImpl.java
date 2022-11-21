package services.taxes.implementations;

import exceptions.ParkingSpotTypeNotFoundException;
import models.parkings.spots.ParkingSpotType;
import services.taxes.ParkingSpotTypePrice;
import utils.Constants;

public class ParkingSpotTypePriceImpl implements ParkingSpotTypePrice {

    @Override
    public double getParkingSpotTypePrice(ParkingSpotType parkingSpotType) throws ParkingSpotTypeNotFoundException {
        switch (parkingSpotType){
            case SMALL: return Constants.BASE_PRICE_FOR_SMALL_PS_TYPE;
            case MEDIUM: return Constants.BASE_PRICE_FOR_MEDIUM_PS_TYPE;
            case LARGE: return Constants.BASE_PRICE_FOR_LARGE_PS_TYPE;
            default: throw new ParkingSpotTypeNotFoundException("Parking spot type " + parkingSpotType + " does not exist.");
        }
    }
}
