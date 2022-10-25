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
	private final ParkingLotRepository parkingLotRepository;
	public ParkingLotManager(ParkingLotRepository parkingLot) {
		this.parkingLotRepository = parkingLot;
	}
	public Ticket park(User user, Vehicle vehicle) throws ParkingSpotNotFound {
		Optional<ParkingSpot> parkingSpotOptional = getParkingSpot(user, vehicle);
		if(parkingSpotOptional.isPresent()){
			parkingSpotOptional.get().setVehicle(vehicle);
			return getTicket(user, vehicle, parkingSpotOptional.get());
		}
		throw new ParkingSpotNotFound("Parking spot not found exception");
	}

	private Ticket getTicket(User user, Vehicle vehicle, ParkingSpot parkingSpot){
		return new Ticket(user, vehicle, parkingSpot);
	}
	private Optional<ParkingSpot> getParkingSpot(User user, Vehicle vehicle) {
		ParkingStrategy parkingStrategy = getParkingStrategy(user);
		return parkingStrategy.getParkingSpot(vehicle, parkingLotRepository);
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

	public Optional<ParkingSpot> findVehicleByPlateNumber(String plateNumber) {
		return parkingLotRepository.findVehicleByPlateNumber(plateNumber);
	}
}
