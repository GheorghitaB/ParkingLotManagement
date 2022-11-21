package models.parkings.spots;

import java.util.Arrays;

public enum ParkingSpotType {
	SMALL,
	MEDIUM,
	LARGE;

	public static boolean containsMember(String parkingSpotType){
		return Arrays.toString(ParkingSpotType.values()).contains(parkingSpotType.toUpperCase());
	}
}
