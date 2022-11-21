package services.taxes.implementations;

import exceptions.ParkingSpotTypeNotFoundException;
import exceptions.UserTypeNotFoundException;
import exceptions.VehicleTypeNotFoundException;
import models.parkings.spots.ParkingSpot;
import models.parkings.spots.ParkingSpotType;
import models.users.User;
import models.users.UserType;
import models.vehicles.Vehicle;
import models.vehicles.VehicleType;
import services.taxes.*;
import utils.Constants;

public class ParkingPriceCalculatorImpl implements ParkingPriceCalculator {

    private final UserTypePrice USER_TYPE_PRICE;
    private final VehicleTypePrice VEHICLE_TYPE_PRICE;
    private final ParkingSpotTypePrice PARKING_SPOT_TYPE_PRICE;
    private final DiscountCalculator DISCOUNT_CALCULATOR;

    public ParkingPriceCalculatorImpl(UserTypePrice USER_TYPE_PRICE,
                                      VehicleTypePrice VEHICLE_TYPE_PRICE, ParkingSpotTypePrice PARKING_SPOT_TYPE_PRICE,
                                      DiscountCalculator DISCOUNT_CALCULATOR) {
        this.USER_TYPE_PRICE = USER_TYPE_PRICE;
        this.VEHICLE_TYPE_PRICE = VEHICLE_TYPE_PRICE;
        this.PARKING_SPOT_TYPE_PRICE = PARKING_SPOT_TYPE_PRICE;
        this.DISCOUNT_CALCULATOR = DISCOUNT_CALCULATOR;
    }

    // TODO Add constraint for parkingDurationInMinutes parameter. It should be > 0.
    @Override
    public double getTotalPrice(int parkingDurationInMinutes, User user, Vehicle vehicle, ParkingSpot parkingSpot)
            throws UserTypeNotFoundException, ParkingSpotTypeNotFoundException, VehicleTypeNotFoundException {
        UserType userType = user.getUserType();
        VehicleType vehicleType = vehicle.getVehicleType();
        ParkingSpotType parkingSpotType = parkingSpot.getParkingSpotType();

        int hours = parkingDurationInMinutes / 60;
        int minutes = parkingDurationInMinutes % 60;
        if(minutes > 30 || (hours == 0 && minutes > 0)) {
            hours += 1;
        }

        double totalPrice = hours * getTotalPriceWithoutDiscountForOneHour(userType, vehicleType, parkingSpotType);

        if(isAbleForDiscount(parkingDurationInMinutes)){
            System.out.println("TOTAL: " + totalPrice);
            System.out.println("Discount: " + DISCOUNT_CALCULATOR.getDiscount(totalPrice, userType));
            totalPrice -= DISCOUNT_CALCULATOR.getDiscount(totalPrice, userType);
            System.out.println("User type: " + userType);
        }

        return totalPrice;
    }

    public double getTotalPriceWithoutDiscountForOneHour(UserType userType, VehicleType vehicleType, ParkingSpotType parkingSpotType)
            throws VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        return USER_TYPE_PRICE.getUserTypePrice(userType)
                + VEHICLE_TYPE_PRICE.getVehicleTypePrice(vehicleType)
                + PARKING_SPOT_TYPE_PRICE.getParkingSpotTypePrice(parkingSpotType);
    }

    private boolean isAbleForDiscount(int parkingDurationInMinutes){
        return parkingDurationInMinutes > Constants.DISCOUNT_AVAILABLE_AFTER_THIS_TIME;
    }
}
