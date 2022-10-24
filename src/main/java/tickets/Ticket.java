package tickets;

import parkingspots.ParkingSpot;
import users.User;
import vehicles.Vehicle;

public class Ticket {
	
	private final User user;
	private final Vehicle vehicle;
	private final ParkingSpot parkingSpot;
	
	public Ticket(User user, Vehicle vehicle, ParkingSpot parkingSpot) {
		this.user = user;
		this.vehicle = vehicle;
		this.parkingSpot = parkingSpot;
	}

	@Override
	public String toString() {
		return "Ticket{" +
				"user=" + user +
				", vehicle=" + vehicle +
				", parkingSpot=" + parkingSpot +
				'}';
	}
}
