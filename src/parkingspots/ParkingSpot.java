package parkingspots;

public abstract class ParkingSpot {
	private final ParkingSpotType parkingSpotType;
	
	protected ParkingSpot(ParkingSpotType parkingSpotType) {
		this.parkingSpotType = parkingSpotType;
	}
	
	public ParkingSpotType getParkingSpotType() {
		return parkingSpotType;
	}
}
