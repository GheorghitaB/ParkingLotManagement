package users;

import java.util.Arrays;

public enum UserType {
	REGULAR,
	VIP;

	public static boolean containsMember(String userType){
		return Arrays.toString(UserType.values()).contains(userType.toUpperCase());
	}
}
