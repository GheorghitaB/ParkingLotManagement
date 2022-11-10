package parkingspots;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ParkingSpotType {
	SMALL,
	MEDIUM,
	LARGE;

	public static boolean containsMember(String parkingSpotType){
		return Stream.of(ParkingSpotType.values())
				.map(member -> member.toString().toUpperCase())
				.collect(Collectors.toList())
				.contains(parkingSpotType.toUpperCase());
	}
}
