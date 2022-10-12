package parkinglots;

import parkingspots.*;

import java.util.HashMap;
import java.util.Map;

public class ParkingLotMapImpl implements ParkingLot {

    public final static int SMALL_PARKING_SPOTS = 1;
    public final static int MEDIUM_PARKING_SPOTS = 1;
    public final static int LARGE_PARKING_SPOTS = 1;
    private Map<ParkingSpotType, Integer> parkingSpots;

    public ParkingLotMapImpl(){
        initParkingLot();
    }

    @Override
    public void initParkingLot() {
        parkingSpots = new HashMap<>();
        parkingSpots.put(ParkingSpotType.SMALL, SMALL_PARKING_SPOTS);
        parkingSpots.put(ParkingSpotType.MEDIUM, MEDIUM_PARKING_SPOTS);
        parkingSpots.put(ParkingSpotType.LARGE, LARGE_PARKING_SPOTS);
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
