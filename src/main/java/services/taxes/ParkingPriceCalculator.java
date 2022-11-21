package services.taxes;

import exceptions.ParkingSpotTypeNotFoundException;
import exceptions.UserTypeNotFoundException;
import exceptions.VehicleTypeNotFoundException;
import models.parkings.spots.ParkingSpot;
import models.users.User;
import models.vehicles.Vehicle;

public interface ParkingPriceCalculator {
    double getTotalPrice(int parkingDurationInMinutes, User user, Vehicle vehicle, ParkingSpot parkingSpot)
            throws UserTypeNotFoundException, ParkingSpotTypeNotFoundException, VehicleTypeNotFoundException;
}
