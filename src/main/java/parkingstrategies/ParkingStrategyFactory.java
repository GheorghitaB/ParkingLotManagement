package parkingstrategies;

import exceptions.UnknownUserStrategy;
import parkinglots.ParkingLotRepository;
import users.User;
import users.UserType;

public class ParkingStrategyFactory {
    private static final ParkingStrategyFactory parkingStrategyInstance = new ParkingStrategyFactory();

    private ParkingStrategyFactory(){}

    public static ParkingStrategyFactory getParkingStrategyInstance(){
        return parkingStrategyInstance;
    }

    public ParkingStrategy getParkingStrategy(User user, ParkingLotRepository parkingLotRepository) throws UnknownUserStrategy {
        UserType userType = user.getUserType();
        switch (userType){
            case REGULAR:
                return new RegularUserParkingStrategy(parkingLotRepository);
            case VIP:
                return new VipUserParkingStrategy(parkingLotRepository);
            default:
                throw new UnknownUserStrategy("Unknown user strategy " + user.getUserType());
        }
    }
}
