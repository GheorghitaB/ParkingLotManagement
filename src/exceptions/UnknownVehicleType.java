package exceptions;

public class UnknownVehicleType extends IllegalStateException{
	public UnknownVehicleType(String s) {
		super(s);
	}
}
