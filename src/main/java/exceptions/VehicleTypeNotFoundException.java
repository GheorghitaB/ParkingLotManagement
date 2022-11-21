package exceptions;

public class VehicleTypeNotFoundException extends ParkingLotException{
    public VehicleTypeNotFoundException(String msg) {
        super(msg);
    }
}
