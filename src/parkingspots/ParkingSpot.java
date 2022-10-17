package parkingspots;

import vehicles.Vehicle;

public abstract class ParkingSpot {
	private int id;
	private static int gen_id = 0;
	private Vehicle vehicle;
	private final ParkingSpotType parkingSpotType;
	private boolean hasElectricCharger;
	protected ParkingSpot(ParkingSpotType parkingSpotType, boolean hasElectricCharger) {
		this.parkingSpotType = parkingSpotType;
		this.hasElectricCharger = hasElectricCharger;
		this.id = getNextId();
	}

	public int getId(){
		return id;
	}
	public Vehicle getVehicle(){
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle){
		this.vehicle = vehicle;
	}
	public ParkingSpotType getParkingSpotType() {
		return parkingSpotType;
	}
	private int getNextId(){
		return gen_id++;
	}
	public boolean isEmpty(){return vehicle == null;}
	public boolean hasElectricCharger(){return hasElectricCharger;}
}
