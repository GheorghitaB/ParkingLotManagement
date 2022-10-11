package parkingstrategies;

import java.util.HashMap;
import java.util.Map;

import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;
import vehicles.Vehicle;

public class ParkingContext {
	private ParkingConditionsStrategy strategy;
	public ParkingContext(ParkingConditionsStrategy strategy) {
		this.strategy = strategy;
	}
	public void checkParkingConditions(Vehicle vehicle, ParkingSpot parkingSpot, Map<ParkingSpotType, Integer> availableParkingSpots) throws Exception {
		 strategy.checkParkingConditions(vehicle, parkingSpot, availableParkingSpots);
	}
}