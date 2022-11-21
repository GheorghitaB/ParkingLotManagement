package exceptions;

public class UserTypeNotFoundException extends ParkingLotException{
    public UserTypeNotFoundException(String msg) {
        super(msg);
    }
}
