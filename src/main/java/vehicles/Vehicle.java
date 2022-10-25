package vehicles;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vehicle vehicle = (Vehicle) o;
		return isElectric == vehicle.isElectric && vehicleType == vehicle.vehicleType && plateNumber.equals(vehicle.plateNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(vehicleType, plateNumber, isElectric);
	}
}
