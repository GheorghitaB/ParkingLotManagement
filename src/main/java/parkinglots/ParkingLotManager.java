package parkinglots;

import exceptions.ParkingSpotNotFound;
import parkingspots.ParkingSpot;
import parkingstrategies.*;
import repositories.ParkingSpotRepository;
import tickets.Ticket;
import users.User;
import vehicles.Vehicle;

import java.util.Optional;

public class ParkingLotManager{
	private final ParkingSpotRepository parkingSpotRepository;
	private final ParkingStrategyFactory parkingStrategyFactory;

	public ParkingLotManager(ParkingSpotRepository parkingSpotRepository, ParkingStrategyFactory parkingStrategyFactory) {
		this.parkingSpotRepository = parkingSpotRepository;
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
		ParkingStrategy parkingStrategy = parkingStrategyFactory.getParkingStrategy(user, parkingSpotRepository);
		return parkingStrategy.getParkingSpot(vehicle);
	}

	public Optional<ParkingSpot> findVehicleByPlateNumber(String plateNumber) {
		return parkingSpotRepository.findVehicleByPlateNumber(plateNumber);
	}
}
