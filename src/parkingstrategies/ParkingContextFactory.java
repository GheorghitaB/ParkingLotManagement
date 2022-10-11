package parkingstrategies;

import exceptions.UnknownParkingStrategy;
import users.User;

public class ParkingContextFactory {
    private static ParkingContextFactory instance;
    private ParkingContextFactory(){}

    public static ParkingContextFactory getInstance(){
        if(instance == null){
            instance = new ParkingContextFactory();
        }
        return instance;
    }

    public ParkingContext getParkingContextForUser(User user) throws UnknownParkingStrategy {
        ParkingContext parkingContext = null;

        switch(user.getUserType()) {
            case REGULAR:
                parkingContext = new ParkingContext(new RegularUserParkingStrategy());
                break;
            case VIP:
                parkingContext = new ParkingContext(new VIPUserParkingStrategy());
                break;
            default:
                throw new UnknownParkingStrategy("Unknown parking strategy for user " + user.getUserType() + " type");
        }

        return parkingContext;
    }

}
