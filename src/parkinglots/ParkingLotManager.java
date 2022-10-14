package parkinglots;

import exceptions.*;
import parkingspots.ParkingSpot;
import parkingstrategies.*;
import tickets.Ticket;
import users.User;
import users.UserType;
import vehicles.Vehicle;

public class ParkingLotManager{
	private final ParkingLotDAO parkingLot;
	public ParkingLotManager(ParkingLotDAO parkingLot) {
		this.parkingLot = parkingLot;
	}
	public Ticket park(User user, Vehicle vehicle) throws ParkingLotException {
		ParkingSpot parkingSpot = getParkingSpot(user, vehicle);
		parkingLot.decreaseParkingSpotsByOne(parkingSpot);
		return new Ticket(user, vehicle, parkingSpot);
	}

	private ParkingStrategy getParkingStrategy(User user) throws UnknownUserStrategy {
		UserType userType = user.getUserType();
		switch (userType){
			case REGULAR:
				return new RegularUserParkingStrategy();
			case VIP:
				return new VipUserParkingStrategy();
			default:
				throw new UnknownUserStrategy("Unknown user strategy " + user.getUserType());
		}
	}

	private ParkingSpot getParkingSpot(User user, Vehicle vehicle) throws ParkingLotException {
		ParkingStrategy parkingStrategy = getParkingStrategy(user);
		return parkingStrategy.getParkingSpotForVehicle(vehicle, parkingLot);
	}
}
