package services.taxes;

import exceptions.ParkingSpotTypeNotFoundException;
import models.parkings.spots.ParkingSpotType;

public interface ParkingSpotTypePrice {
    double getParkingSpotTypePrice(ParkingSpotType parkingSpotType) throws ParkingSpotTypeNotFoundException;
}
