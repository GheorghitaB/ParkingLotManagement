package users;

public class UserFactory {
	private static UserFactory instance;
	
	private UserFactory() {}
	
	public static UserFactory getUserFactoryInstance() {
		if(instance == null) {
			instance = new UserFactory();
		}
		return instance;
	}
	
	public User getUser(UserType userType, String name) {
		switch(userType) {
		case REGULAR:
			return new RegularUser(name);
		case VIP:
			return new VIPUser(name);
		default:
			return null;
		}
	}
}
