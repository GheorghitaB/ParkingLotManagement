package parkinglots;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import exceptions.FullParkingLotException;
import exceptions.InvalidParkingSpotName;
import exceptions.InvalidParkingSpotSelection;
import exceptions.UnknownParkingStrategy;
import exceptions.UnknownVehicleType;
import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotFactory;
import parkingspots.ParkingSpotType;
import parkingstrategies.*;
import tickets.Ticket;
import users.User;
import users.UserFactory;
import users.UserType;
import vehicles.Vehicle;
import vehicles.VehicleFactory;
import vehicles.VehicleType;

public class ParkingLotManager{
	private Map<ParkingSpotType, Integer> availableParkingSpots;

	public ParkingLotManager() {
		initAvailableParkingSpots();
	}
	private void initAvailableParkingSpots() {
		availableParkingSpots = new HashMap<>();
		availableParkingSpots.put(ParkingSpotType.SMALL, 1);
		availableParkingSpots.put(ParkingSpotType.MEDIUM, 1);
		availableParkingSpots.put(ParkingSpotType.LARGE, 1);
	}

	public void acceptForParking(User user, Vehicle vehicle, ParkingSpot parkingSpot) {
		try {
			checkParkingConditions(user, vehicle, parkingSpot);
			Ticket ticket = park(user, vehicle, parkingSpot);
			ticket.printTicket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkParkingConditions(User user, Vehicle vehicle, ParkingSpot parkingSpot) throws Exception {
		ParkingContext parkingContext = getParkingContextForUser(user);
		parkingContext.checkParkingConditions(vehicle, parkingSpot, availableParkingSpots);
	}

	private ParkingContext getParkingContextForUser(User user){
		ParkingContextFactory parkingContextFactory = ParkingContextFactory.getInstance();
		ParkingContext parkingContext = null;
		try {
			parkingContext = parkingContextFactory.getParkingContextForUser(user);
		} catch (UnknownParkingStrategy e) {
			e.printStackTrace();
		}
		return parkingContext;
	}

	private Ticket park(User user, Vehicle vehicle, ParkingSpot parkingSpot) {
		Ticket ticket = new Ticket(user, vehicle, parkingSpot);
		availableParkingSpots.put(parkingSpot.getParkingSpotType(), availableParkingSpots.get(parkingSpot.getParkingSpotType())-1);
		return ticket;
	}
}
