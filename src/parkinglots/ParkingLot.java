package parkinglots;

import parkingspots.ParkingSpot;
import tickets.Ticket;
import users.User;
import vehicles.Vehicle;

public interface ParkingLot {
    Ticket park(User user, Vehicle vehicle, ParkingSpot parkingSpot);
}
