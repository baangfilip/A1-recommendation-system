package se.kb222vt.entities;

public class UserEntity {
	private String userName = "";
	private int userID;
	
	public UserEntity(String userName, int userID) {
		this.userName = userName;
		this.userID = userID;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
