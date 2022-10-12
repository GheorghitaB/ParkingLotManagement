package parkingstrategies;

import java.util.Map;

import exceptions.FullParkingLotException;
import exceptions.InvalidParkingSpotName;
import exceptions.InvalidParkingSpotSelection;
import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;
import vehicles.Vehicle;
import vehicles.VehicleType;

public class RegularUserParkingStrategy implements ParkingConditionsStrategy {
	
	@Override
	public void checkParkingConditions(Vehicle vehicle, ParkingSpot parkingSpot, Map<ParkingSpotType, Integer> availableParkingSpots) throws Exception {

		ParkingSpotType selectedParkingSpotType = parkingSpot.getParkingSpotType();
		VehicleType selectedVehicleType = vehicle.getVehicleType();
		int availableParkingSpotSize = availableParkingSpots.get(selectedParkingSpotType);

		checkAvailableParkingSpotSize(availableParkingSpotSize);

		if(selectedParkingSpotType == ParkingSpotType.SMALL) {
			checkParkingConditionsForSmallParkingSpotType(selectedVehicleType);
		} else if(selectedParkingSpotType == ParkingSpotType.MEDIUM){
			checkParkingConditionsForMediumParkingSpotType(selectedVehicleType);
		} else if(selectedParkingSpotType == ParkingSpotType.LARGE){
			checkParkingConditionForLargeParkingSpotType(selectedVehicleType);
		} else {
			throw new InvalidParkingSpotName("Invalid parking spot name");
		}
	}

	private void checkAvailableParkingSpotSize(int availableParkingSpotSize) throws FullParkingLotException {
		if(availableParkingSpotSize <= 0)
			throw new FullParkingLotException("Unavailable parking spot");
	}

	private void checkParkingConditionsForSmallParkingSpotType(VehicleType vehicleType) throws InvalidParkingSpotSelection {
		if(vehicleType != VehicleType.MOTORCYCLE) {
			throw new InvalidParkingSpotSelection("Invalid parking spot selection");
		}
	}

	private void checkParkingConditionsForMediumParkingSpotType(VehicleType vehicleType) throws InvalidParkingSpotSelection {
		if(vehicleType != VehicleType.CAR) {
			throw new InvalidParkingSpotSelection("Invalid parking spot selection");
		}
	}

	private void checkParkingConditionForLargeParkingSpotType(VehicleType vehicleType) throws InvalidParkingSpotSelection {
		if(vehicleType != VehicleType.TRUCK) {
			throw new InvalidParkingSpotSelection("Invalid parking spot selection");
		}
	}
}
