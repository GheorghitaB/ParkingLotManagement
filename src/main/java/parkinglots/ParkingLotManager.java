package parkinglots;

import exceptions.ParkingSpotNotFound;
import parkingspots.ParkingSpot;
import parkingstrategies.*;
import repositories.ParkingSpotService;
import tickets.Ticket;
import users.User;
import vehicles.Vehicle;

import java.util.Optional;

public class ParkingLotManager{
	private final ParkingSpotService parkingSpotService;
	private final ParkingStrategyFactory parkingStrategyFactory;

	public ParkingLotManager(ParkingSpotService parkingSpotService, ParkingStrategyFactory parkingStrategyFactory) {
		this.parkingSpotService = parkingSpotService;
		this.parkingStrategyFactory = parkingStrategyFactory;
	}

	public Ticket park(User user, Vehicle vehicle) throws ParkingSpotNotFound {
		Optional<ParkingSpot> parkingSpotOptional = getParkingSpot(user, vehicle);
		if(parkingSpotOptional.isPresent()){
			parkingSpotOptional.get().setVehicle(vehicle);
			return new Ticket(user, vehicle, parkingSpotOptional.get());
		}

		throw new ParkingSpotNotFound("Parking spot not found exception");
	}

	private Optional<ParkingSpot> getParkingSpot(User user, Vehicle vehicle) {
		ParkingStrategy parkingStrategy = parkingStrategyFactory.getParkingStrategy(user);
		return parkingStrategy.getParkingSpot(vehicle, parkingSpotService);
	}

	public Optional<ParkingSpot> findVehicleByPlateNumber(String plateNumber) {
		return parkingSpotService.findVehicleByPlateNumber(plateNumber);
	}
}
