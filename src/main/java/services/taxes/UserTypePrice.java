package services.taxes;

import exceptions.UserTypeNotFoundException;
import models.users.UserType;

public interface UserTypePrice {
    double getUserTypePrice(UserType userType) throws UserTypeNotFoundException;
}
