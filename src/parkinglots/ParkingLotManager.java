package parkinglots;

import exceptions.*;
import parkingspots.ParkingSpot;
import parkingstrategies.*;
import tickets.Ticket;
import users.User;
import users.UserType;
import vehicles.Vehicle;

public class ParkingLotManager{
	private final ParkingLot parkingLot;
	public ParkingLotManager(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
	}
	public Ticket park(User user, Vehicle vehicle) throws ParkingLotException {
		UserStrategy userStrategy = getUserStrategy(user);
		ParkingSpot parkingSpot = getParkingSpot(userStrategy, vehicle);
		parkingLot.decreaseParkingSpotsByOne(parkingSpot);
		return new Ticket(user, vehicle, parkingSpot);
	}

	private UserStrategy getUserStrategy(User user) throws UnknownUserStrategy {
		UserType userType = user.getUserType();
		switch (userType){
			case REGULAR:
				return new RegularUserStrategy();
			case VIP:
				return new VipUserStrategy();
			default:
				throw new UnknownUserStrategy("Unknown user strategy " + user.getUserType());
		}
	}

	private ParkingSpot getParkingSpot(UserStrategy userStrategy, Vehicle vehicle) throws ParkingLotException {
		return userStrategy.getParkingSpotForVehicle(vehicle, parkingLot);
	}
}
