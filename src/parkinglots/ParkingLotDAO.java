package parkinglots;

import parkingspots.ParkingSpot;
import parkingspots.ParkingSpotType;

import java.util.Map;

public interface ParkingLotDAO {
    void decreaseParkingSpotsByOne(ParkingSpot parkingSpot);
    void increaseParkingSpotsByValue(ParkingSpot parkingSpot, int value);
    void decreaseParkingSpotsByValue(ParkingSpot parkingSpot, int value);
    Map<ParkingSpotType, Integer> getAllParkingSpots();
    int getParkingSpotSizeByType(ParkingSpotType parkingSpotType);
}
