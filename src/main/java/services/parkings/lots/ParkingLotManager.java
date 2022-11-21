package services.parkings.lots;

import exceptions.ParkingSpotNotFound;
import exceptions.ParkingSpotTypeNotFoundException;
import exceptions.UserTypeNotFoundException;
import exceptions.VehicleTypeNotFoundException;
import models.parkings.spots.ParkingSpot;
import services.parkings.spots.ParkingSpotService;
import models.tickets.Ticket;
import models.users.User;
import models.vehicles.Vehicle;
import services.parkings.strategies.ParkingStrategy;
import services.parkings.strategies.ParkingStrategyFactory;
import services.taxes.ParkingPriceCalculator;

import java.util.Optional;

public class ParkingLotManager{
	private final ParkingSpotService parkingSpotService;
	private final ParkingStrategyFactory parkingStrategyFactory;
	private final ParkingPriceCalculator parkingPriceCalculator;

	public ParkingLotManager(ParkingSpotService parkingSpotService, ParkingStrategyFactory parkingStrategyFactory, ParkingPriceCalculator parkingPriceCalculator) {
		this.parkingSpotService = parkingSpotService;
		this.parkingStrategyFactory = parkingStrategyFactory;
		this.parkingPriceCalculator = parkingPriceCalculator;
	}

	public Ticket park(int parkingDurationInMinutes, User user, Vehicle vehicle)
			throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
		Optional<ParkingSpot> parkingSpotOptional = getParkingSpot(user, vehicle);
		if(parkingSpotOptional.isPresent()){
			parkingSpotOptional.get().setVehicle(vehicle);
			double price = parkingPriceCalculator.getTotalPrice(parkingDurationInMinutes, user, vehicle, parkingSpotOptional.get());
			return new Ticket(user, vehicle, parkingSpotOptional.get(), price, parkingDurationInMinutes);
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
