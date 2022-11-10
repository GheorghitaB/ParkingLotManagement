package users;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum UserType {
	REGULAR,
	VIP;

	public static boolean containsMember(String userType){
		return Stream.of(UserType.values())
				.map(member -> member.toString().toUpperCase())
				.collect(Collectors.toList())
				.contains(userType.toUpperCase());
	}
}
