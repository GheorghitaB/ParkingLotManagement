package services.taxes.implementations;

import exceptions.UserTypeNotFoundException;
import models.users.UserType;
import services.taxes.DiscountCalculator;
import utils.Constants;

public class DiscountCalculatorImpl implements DiscountCalculator {
    @Override
    public double getDiscount(double totalPrice, UserType userType) throws UserTypeNotFoundException {
        switch (userType){
            case REGULAR: return Constants.REGULAR_USER_DISCOUNT * totalPrice;
            case VIP: return Constants.VIP_USER_DISCOUNT * totalPrice;
            default: throw new UserTypeNotFoundException("User type " + userType + " does not exist.");
        }
    }
}
