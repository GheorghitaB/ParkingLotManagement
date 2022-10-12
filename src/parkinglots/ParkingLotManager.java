package parkinglots;

import java.util.HashMap;
import java.util.Map;

import exceptions.*;
import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;
import parkingstrategies.*;
import tickets.Ticket;
import users.User;
import vehicles.Vehicle;

public class ParkingLotManager{
	private Map<ParkingSpotType, Integer> availableParkingSpots;
	private final ExceptionHandler handler;

	public final static int SMALL_PARKING_SPOTS = 1;
	public final static int MEDIUM_PARKING_SPOTS = 1;
	public final static int LARGE_PARKING_SPOTS = 1;

	public ParkingLotManager() {
		handler = new ExceptionHandler();
		initAvailableParkingSpots();
	}
	private void initAvailableParkingSpots() {
		availableParkingSpots = new HashMap<>();
		availableParkingSpots.put(ParkingSpotType.SMALL, SMALL_PARKING_SPOTS);
		availableParkingSpots.put(ParkingSpotType.MEDIUM, MEDIUM_PARKING_SPOTS);
		availableParkingSpots.put(ParkingSpotType.LARGE, LARGE_PARKING_SPOTS);
	}

	public void acceptForParking(User user, Vehicle vehicle, ParkingSpot parkingSpot) {
		try {
			checkParkingConditions(user, vehicle, parkingSpot);
			Ticket ticket = park(user, vehicle, parkingSpot);
			ticket.printTicket();
		} catch (Exception e) {
			handler.handleException(e);
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
			handler.handleException(e);
		}
		return parkingContext;
	}

	private Ticket park(User user, Vehicle vehicle, ParkingSpot parkingSpot) {
		Ticket ticket = new Ticket(user, vehicle, parkingSpot);
		availableParkingSpots.put(parkingSpot.getParkingSpotType(), availableParkingSpots.get(parkingSpot.getParkingSpotType())-1);
		return ticket;
	}
}
