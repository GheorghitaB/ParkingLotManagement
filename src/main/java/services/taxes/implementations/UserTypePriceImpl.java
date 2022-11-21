package services.taxes.implementations;

import exceptions.UserTypeNotFoundException;
import models.users.UserType;
import services.taxes.UserTypePrice;
import utils.Constants;

public class UserTypePriceImpl implements UserTypePrice {
    @Override
    public double getUserTypePrice(UserType userType) throws UserTypeNotFoundException {
        switch (userType){
            case REGULAR: return Constants.BASE_PRICE_FOR_REGULAR_USER;
            case VIP: return Constants.BASE_PRICE_FOR_VIP_USER;
            default: throw new UserTypeNotFoundException("User type " + userType + " does not exist.");
        }
    }
}
