package parkinglots;

import exceptions.ParkingSpotNotFound;
import parkingspots.ParkingSpot;
import parkingstrategies.*;
import tickets.Ticket;
import users.User;
import vehicles.Vehicle;

import java.util.Optional;

public class ParkingLotManager{
	private final ParkingLotRepository parkingLotRepository;
	private final ParkingStrategyFactory parkingStrategyFactory;

	public ParkingLotManager(ParkingLotRepository parkingLot, ParkingStrategyFactory parkingStrategyFactory) {
		this.parkingLotRepository = parkingLot;
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
		ParkingStrategy parkingStrategy = parkingStrategyFactory.getParkingStrategy(user, parkingLotRepository);
		return parkingStrategy.getParkingSpot(vehicle);
	}

	public Optional<ParkingSpot> findVehicleByPlateNumber(String plateNumber) {
		return parkingLotRepository.findVehicleByPlateNumber(plateNumber);
	}
}
