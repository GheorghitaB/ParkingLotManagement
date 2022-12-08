package models.parkings.spots;

public class MediumParkingSpot extends ParkingSpot{
	public MediumParkingSpot(boolean hasElectricCharger) {
		super(ParkingSpotType.MEDIUM, hasElectricCharger);
	}
}
