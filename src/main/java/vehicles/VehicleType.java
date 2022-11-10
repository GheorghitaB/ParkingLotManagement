package vehicles;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum VehicleType {
	MOTORCYCLE,
	CAR,
	TRUCK;

	public static boolean containsMember(String vehicleType) {
		return Stream.of(VehicleType.values())
				.map(member -> member.toString().toUpperCase())
				.collect(Collectors.toList())
				.contains(vehicleType.toUpperCase());
	}
}
