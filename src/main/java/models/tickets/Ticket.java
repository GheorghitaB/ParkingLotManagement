package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import models.parkings.spots.ParkingSpot;
import models.prices.Price;
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
	private final Price price;
	private final int parkingDurationTimeInMinutes;

	//TODO Create another method / class to take care of the ID attribute
	public Ticket(User user, Vehicle vehicle, ParkingSpot parkingSpot, Price price, int parkingDurationTimeInMinutes) {
		this.user = user;
		this.vehicle = vehicle;
		this.parkingSpot = parkingSpot;
		this.price = price;
		this.parkingDurationTimeInMinutes = parkingDurationTimeInMinutes;
		ID = getNextId();
	}

	private int getNextId() {
		return gen_id++;
	}

	@Override
	public String toString() {
		return "Ticket{" +
				"ID=" + ID +
				", user=" + user +
				", vehicle=" + vehicle +
				", parkingSpot=" + parkingSpot +
				", price=" + price +
				", parkingDurationTimeInMinutes=" + parkingDurationTimeInMinutes +
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

	public Price getPrice() {
		return price;
	}

	public int getParkingDurationTimeInMinutes() {
		return parkingDurationTimeInMinutes;
	}

	public int getID(){
		return ID;
	}
}
