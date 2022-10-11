package parkingstrategies;

import java.util.HashMap;
import java.util.Map;

import exceptions.FullParkingLotException;
import exceptions.InvalidParkingSpotName;
import exceptions.InvalidParkingSpotSelection;
import exceptions.UnknownVehicleType;
import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;
import vehicles.Vehicle;
import vehicles.VehicleType;

public class VIPUserParkingStrategy implements ParkingConditionsStrategy {

    @Override
    public void checkParkingConditions(Vehicle vehicle, ParkingSpot parkingSpot, Map<ParkingSpotType, Integer> availableParkingSpots) throws Exception {
        VehicleType selectedVehicleType = vehicle.getVehicleType();
        ParkingSpotType selectedParkingSpot = parkingSpot.getParkingSpotType();
        int smallParkingSpots = availableParkingSpots.get(ParkingSpotType.SMALL);
        int mediumParkingSpots = availableParkingSpots.get(ParkingSpotType.MEDIUM);
        int largeParkingSpots = availableParkingSpots.get(ParkingSpotType.LARGE);

        if (selectedVehicleType == VehicleType.MOTORCYCLE) {
            checkMotorcycleParkingConditions(selectedParkingSpot, smallParkingSpots, mediumParkingSpots, largeParkingSpots);
        } else if (selectedVehicleType == VehicleType.CAR) {
            checkCarParkingConditions(selectedParkingSpot, smallParkingSpots, mediumParkingSpots, largeParkingSpots);
        } else if (selectedVehicleType == VehicleType.TRUCK) {
            checkTruckParkingConditions(selectedParkingSpot, smallParkingSpots, mediumParkingSpots, largeParkingSpots);
        } else throw new UnknownVehicleType("Unknown vehicle type");
    }

    private void checkMotorcycleParkingConditions(ParkingSpotType selectedParkingSpot, int smallParkingSpots, int mediumParkingSpots, int largeParkingSpots) throws Exception {
        switch (selectedParkingSpot) {
            case SMALL:
                if (smallParkingSpots <= 0)
                    throw new FullParkingLotException("Unavailable parking spot");
                break;
            case MEDIUM:
                if (smallParkingSpots > 0)
                    throw new InvalidParkingSpotSelection("Invalid parking spot selection");
                if (mediumParkingSpots <= 0)
                    throw new FullParkingLotException("Unavailable parking spot");
                break;
            case LARGE:
                if (smallParkingSpots > 0)
                    throw new InvalidParkingSpotSelection("Invalid parking spot selection");
                if (largeParkingSpots <= 0)
                    throw new FullParkingLotException("Unavailable parking spot");
                break;
            default:
                throw new InvalidParkingSpotName("Invalid parking spot name");
        }
    }

    private void checkCarParkingConditions(ParkingSpotType selectedParkingSpot, int smallParkingSpots, int mediumParkingSpots, int largeParkingSpots) throws Exception {
        switch (selectedParkingSpot) {
            case SMALL:
                throw new InvalidParkingSpotSelection("Invalid parking spot selection");
            case MEDIUM:
                if (mediumParkingSpots <= 0)
                    throw new FullParkingLotException("Unavailable parking spot");
                break;
            case LARGE:
                if (mediumParkingSpots > 0)
                    throw new InvalidParkingSpotSelection("Invalid parking spot selection");
                if (largeParkingSpots <= 0)
                    throw new FullParkingLotException("Unavailable parking spot");
                break;
            default:
                throw new InvalidParkingSpotName("Invalid parking spot name");
        }
    }

    private void checkTruckParkingConditions(ParkingSpotType selectedParkingSpot, int smallParkingSpots, int mediumParkingSpots, int largeParkingSpots) throws Exception {
        switch (selectedParkingSpot) {
            case SMALL:
            case MEDIUM:
                throw new InvalidParkingSpotSelection("Invalid parking spot selection");
            case LARGE:
                if (largeParkingSpots <= 0)
                    throw new FullParkingLotException("Unavailable parking spot");
                break;
            default:
                throw new InvalidParkingSpotName("Invalid parking spot name");
        }
    }
}