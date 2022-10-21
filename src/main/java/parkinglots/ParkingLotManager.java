package parkinglots;

import exceptions.*;
import parkingspots.ParkingSpot;
import parkingstrategies.*;
import tickets.Ticket;
import users.User;
import users.UserType;
import vehicles.Vehicle;

import java.util.Optional;

public class ParkingLotManager{
	private final ParkingLotRepository parkingLot;
	public ParkingLotManager(ParkingLotRepository parkingLot) {
		this.parkingLot = parkingLot;
	}
	public Ticket park(User user, Vehicle vehicle) throws ParkingSpotNotFound {
		Optional<ParkingSpot> parkingSpot = getParkingSpot(user, vehicle);
		parkingSpot.get().setVehicle(vehicle);
		return new Ticket(user, vehicle, parkingSpot.get());
	}

	private Optional<ParkingSpot> getParkingSpot(User user, Vehicle vehicle) throws ParkingSpotNotFound {
		ParkingStrategy parkingStrategy = getParkingStrategy(user);
		return parkingStrategy.getParkingSpot(vehicle, parkingLot);
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

	public ParkingSpot findVehicle(Vehicle vehicle) throws VehicleNotFound {
		return parkingLot.findVehicle(vehicle);
	}

}
