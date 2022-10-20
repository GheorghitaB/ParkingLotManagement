package vehicles;

public abstract class Vehicle {
	private final VehicleType vehicleType;
	private final String plateNumber;
	private final boolean isElectric;
	protected Vehicle(VehicleType vehicleType, String plateNumber, boolean isElectric) {
		this.vehicleType = vehicleType;
		this.plateNumber = plateNumber;
		this.isElectric = isElectric;
	}
	public VehicleType getVehicleType() {
		return vehicleType;
	}
	public String getPlateNumber(){
		return plateNumber;
	}
	public boolean isElectric(){
		return isElectric;
	}
}
