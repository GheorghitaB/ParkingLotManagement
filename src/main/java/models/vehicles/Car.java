package models.vehicles;

public class Car extends Vehicle{

	public Car(String plateNumber, boolean isElectric) {
		super(VehicleType.CAR, plateNumber, isElectric);
	}

}
