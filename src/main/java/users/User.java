package users;

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
	
	
}
