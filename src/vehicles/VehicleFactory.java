package vehicles;

public class VehicleFactory {
	private static VehicleFactory instance;
	
	private VehicleFactory() {}
	
	public static VehicleFactory getVehicleFactoryInstance() {
		if(instance == null) {
			instance = new VehicleFactory();
		}
		return instance;
	}
	
	public Vehicle getVehicle(VehicleType vehicleType) {
		switch(vehicleType) {
		case MOTORCYCLE:
			return new Motorcycle();
		case CAR:
			return new Car();
		case TRUCK:
			return new Truck();
		default:
			return null;
		}
	}
}
