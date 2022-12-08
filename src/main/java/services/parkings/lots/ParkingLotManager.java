package services.parkings.lots;

import exceptions.ParkingSpotNotFound;
import exceptions.PriceException;
import models.parkings.spots.ParkingSpot;
import models.prices.Currency;
import models.prices.Price;
import services.api.prices.PriceService;
import services.parkings.spots.ParkingSpotService;
import models.tickets.Ticket;
import models.users.User;
import models.vehicles.Vehicle;
import services.parkings.strategies.ParkingStrategy;
import services.parkings.strategies.ParkingStrategyFactory;

import java.util.Optional;

public class ParkingLotManager{
	private final ParkingSpotService parkingSpotService;
	private final ParkingStrategyFactory parkingStrategyFactory;

	public ParkingLotManager(ParkingSpotService parkingSpotService, ParkingStrategyFactory parkingStrategyFactory) {
		this.parkingSpotService = parkingSpotService;
		this.parkingStrategyFactory = parkingStrategyFactory;
	}

	public Ticket park(int parkingDurationInMinutes, User user, Vehicle vehicle) throws ParkingSpotNotFound, PriceException {
		Optional<ParkingSpot> parkingSpotOptional = getParkingSpot(user, vehicle);
		if(parkingSpotOptional.isPresent()){
			ParkingSpot parkingSpot = parkingSpotOptional.get();
			parkingSpot.setVehicle(vehicle);

			Optional<Price> price = new PriceService().getPrice(parkingDurationInMinutes, user.getUserType(), vehicle.getVehicleType(),
					parkingSpot.getParkingSpotType(), Currency.EUR);

			if (price.isEmpty()){
				throw new PriceException("Could not calculate the price. Service unavailable.");
			}
			return new Ticket(user, vehicle, parkingSpot, price.get(), parkingDurationInMinutes);
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
