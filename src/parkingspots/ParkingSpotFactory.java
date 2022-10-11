package parkingspots;

public class ParkingSpotFactory {
	private static ParkingSpotFactory parkingSpotInstance;
	
	private ParkingSpotFactory() {}
	
	public static ParkingSpotFactory getParkingSpotInstance() {
		if(parkingSpotInstance == null) {
			parkingSpotInstance = new ParkingSpotFactory();
		}
		
		return parkingSpotInstance;
	}
	
	public ParkingSpot getParkingSpot(ParkingSpotType parkingSpotType) {
		switch(parkingSpotType) {
			case SMALL:
				return new SmallParkingSpot();		
			case MEDIUM:
				return new MediumParkingSpot();
			case LARGE:
				return new LargeParkingSpot();
			default:
				return null;
		}
	}
}
