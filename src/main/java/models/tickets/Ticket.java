package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import models.parkings.spots.ParkingSpot;
import models.users.User;
import models.vehicles.Vehicle;

@JsonIgnoreProperties(value = {"id"})
@JsonTypeInfo(include= JsonTypeInfo.As.WRAPPER_OBJECT, use= JsonTypeInfo.Id.NAME)
public class Ticket {

	private final int ID;
	private static int gen_id = 0;
	private final User user;
	private final Vehicle vehicle;
	private final ParkingSpot parkingSpot;
	
	public Ticket(User user, Vehicle vehicle, ParkingSpot parkingSpot) {
		this.user = user;
		this.vehicle = vehicle;
		this.parkingSpot = parkingSpot;
		ID = getNextId();
	}

	private int getNextId() {
		return gen_id++;
	}

	@Override
	public String toString() {
		return "Ticket{" +
				"user=" + user +
				", vehicle=" + vehicle +
				", parkingSpot=" + parkingSpot +
				", ID=" + ID +
				'}';
	}

	public User getUser(){
		return user;
	}

	public Vehicle getVehicle(){
		return vehicle;
	}

	public ParkingSpot getParkingSpot(){
		return parkingSpot;
	}
	public int getID(){
		return ID;
	}
}
