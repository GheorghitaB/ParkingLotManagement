package models.parkings.spots;

import models.vehicles.Vehicle;

import java.util.Objects;

public abstract class ParkingSpot {
	private final int id;
	private static int gen_id = 0;
	private Vehicle vehicle;
	private final ParkingSpotType parkingSpotType;
	private final boolean hasElectricCharger;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ParkingSpot that = (ParkingSpot) o;
		return id == that.id && hasElectricCharger == that.hasElectricCharger && vehicle.equals(that.vehicle) && parkingSpotType == that.parkingSpotType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, vehicle, parkingSpotType, hasElectricCharger);
	}

	@Override
	public String toString() {
		return "ParkingSpot{" +
				"id=" + id +
				", vehicle=" + vehicle +
				", parkingSpotType=" + parkingSpotType +
				", hasElectricCharger=" + hasElectricCharger +
				'}';
	}
}
