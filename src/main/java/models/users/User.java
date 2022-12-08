package models.users;

import java.util.Objects;

public abstract class User {
	protected String name;
	private final UserType userType;
	
	protected User(String name, UserType userType) {
		this.name = name;
		this.userType = userType;
	}
	
	public String getName() {
		return name;
	}
	
	public UserType getUserType() {
		return userType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return name.equals(user.name) && userType == user.userType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, userType);
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", userType=" + userType +
				'}';
	}
}
