package tickets;

import java.util.Date;

import parkingspots.ParkingSpot;
import users.User;
import users.UserType;
import vehicles.Vehicle;

public class Ticket {
	
	private User user;
	private Vehicle vehicle;
	private ParkingSpot parkingSpot;
	
	public Ticket(User user, Vehicle vehicle, ParkingSpot parkingSpot) {
		this.user = user;
		this.vehicle = vehicle;
		this.parkingSpot = parkingSpot;
	}
	
	public void printTicket() {
		final String delimiter = "---------------------------------------------";
		System.out.println(delimiter);
		System.out.println("Ticket");
		System.out.println("User: " + user.getName() + 
				" (" + user.getUserType().name() + ")"+
				"\nVehicle type: " + vehicle.getVehicleType() + 
				"\nParking spot type: " + parkingSpot.getParkingSpotType());
		System.out.println(delimiter);
		System.out.println("Date: " + new Date());
		System.out.println(delimiter);
	}
}
