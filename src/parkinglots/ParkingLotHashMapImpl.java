package parkinglots;

import parkingspots.*;

import java.util.HashMap;
import java.util.Map;

public class ParkingLotHashMapImpl implements ParkingLotDAO {
    private Map<ParkingSpotType, Integer> parkingSpots;

    public ParkingLotHashMapImpl(Map<ParkingSpotType, Integer> parkingSpots){
        this.parkingSpots = parkingSpots;
    }

    @Override
    public void decreaseParkingSpotsByOne(ParkingSpot parkingSpot) {
        parkingSpots.put(parkingSpot.getParkingSpotType(), parkingSpots.get(parkingSpot.getParkingSpotType())-1);
    }

    @Override
    public void increaseParkingSpotsByValue(ParkingSpot parkingSpot, int value) {
        parkingSpots.put(parkingSpot.getParkingSpotType(), parkingSpots.get(parkingSpot.getParkingSpotType()) + value);
    }

    @Override
    public void decreaseParkingSpotsByValue(ParkingSpot parkingSpot, int value) {
        parkingSpots.put(parkingSpot.getParkingSpotType(), parkingSpots.get(parkingSpot.getParkingSpotType()) - value);
    }

    @Override
    public Map<ParkingSpotType, Integer> getAllParkingSpots() {
        return parkingSpots;
    }

    @Override
    public int getParkingSpotSizeByType(ParkingSpotType parkingSpotType) {
        return parkingSpots.get(parkingSpotType);
    }
}
