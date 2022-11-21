package services.taxes;

import exceptions.UserTypeNotFoundException;
import models.users.UserType;

public interface DiscountCalculator {
    double getDiscount(double totalPrice, UserType userType) throws UserTypeNotFoundException;
}
