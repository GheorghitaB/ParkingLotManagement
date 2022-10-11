package vehicles;

public abstract class Vehicle {
	private final VehicleType vehicleType;
	
	protected Vehicle(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}
	public VehicleType getVehicleType() {
		return vehicleType;
	}
}
