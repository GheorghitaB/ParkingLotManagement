package models.vehicles;

import java.util.Arrays;

public enum VehicleType {
	MOTORCYCLE,
	CAR,
	TRUCK;

	public static boolean containsMember(String vehicleType) {
		return Arrays.toString(VehicleType.values()).contains(vehicleType.toUpperCase());
	}
}
