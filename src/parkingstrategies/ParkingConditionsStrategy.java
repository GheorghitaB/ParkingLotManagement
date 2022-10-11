package parkingstrategies;

import java.util.HashMap;
import java.util.Map;

import exceptions.FullParkingLotException;
import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;
import vehicles.Vehicle;

public interface ParkingConditionsStrategy {
	void checkParkingConditions(Vehicle vehicle, ParkingSpot parkingSpot, Map<ParkingSpotType, Integer> availableParkingSpots) throws Exception;
}
