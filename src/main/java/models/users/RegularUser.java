package models.users;

public class RegularUser extends User{

	public RegularUser(String name) {
		super(name, UserType.REGULAR);
	}
}
