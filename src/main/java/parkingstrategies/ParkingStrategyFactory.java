package parkingstrategies;

import exceptions.UnknownUserStrategy;
import repositories.ParkingSpotRepository;
import users.User;
import users.UserType;

public class ParkingStrategyFactory {
    private static final ParkingStrategyFactory parkingStrategyInstance = new ParkingStrategyFactory();

    private ParkingStrategyFactory(){}

    public static ParkingStrategyFactory getParkingStrategyInstance(){
        return parkingStrategyInstance;
    }

    public ParkingStrategy getParkingStrategy(User user, ParkingSpotRepository parkingSpotRepository) throws UnknownUserStrategy {
        UserType userType = user.getUserType();
        switch (userType){
            case REGULAR:
                return new RegularUserParkingStrategy(parkingSpotRepository);
            case VIP:
                return new VipUserParkingStrategy(parkingSpotRepository);
            default:
                throw new UnknownUserStrategy("Unknown user strategy " + user.getUserType());
        }
    }
}
