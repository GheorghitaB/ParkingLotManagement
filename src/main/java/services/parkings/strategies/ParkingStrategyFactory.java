package services.parkings.strategies;

import exceptions.UnknownUserStrategy;
import models.users.User;
import models.users.UserType;

public class ParkingStrategyFactory {
    private static final ParkingStrategyFactory INSTANCE = new ParkingStrategyFactory();
    private static final ParkingStrategy REGULAR_USER_STRATEGY = new RegularUserParkingStrategy();
    private static final ParkingStrategy VIP_USER_STRATEGY = new VipUserParkingStrategy();

    private ParkingStrategyFactory(){}

    public static ParkingStrategyFactory getParkingStrategyInstance(){
        return INSTANCE;
    }

    public ParkingStrategy getParkingStrategy(User user) throws UnknownUserStrategy {
        UserType userType = user.getUserType();
        switch (userType){
            case REGULAR:
                return REGULAR_USER_STRATEGY;
            case VIP:
                return VIP_USER_STRATEGY;
            default:
                throw new UnknownUserStrategy("Unknown user strategy " + user.getUserType());
        }
    }
}
