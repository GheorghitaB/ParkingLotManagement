package services.api.prices;

import models.parkings.spots.ParkingSpotType;
import models.prices.Currency;
import models.prices.Price;
import models.users.UserType;
import models.vehicles.VehicleType;

import java.util.Optional;

public interface PriceService {
    Optional<Price> getPrice(int parkingTimeInMinutes, UserType userType, VehicleType vehicleType,
                             ParkingSpotType parkingSpotType, Currency toCurrency);
}
