package models;

public class Credential {
	private User user;
	private String password;
	
	public boolean checkCredential(String password, User user) {
		
		if(this.password.equals(password) && this.user.equals(user))
			return true;
		
		return false;
	}
}
