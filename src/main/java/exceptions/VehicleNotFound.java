package exceptions;

public class VehicleNotFound extends ParkingLotException{
    public VehicleNotFound(String msg) {
        super(msg);
    }
}
