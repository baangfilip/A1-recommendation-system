package se.kb222vt.entities;

import java.util.HashMap;

public class UserEntity {
	private String userName = "";
	private int userID;
	private HashMap<String, Double> movieRatings = new HashMap<>();
	
	public UserEntity(String userName, int userID) {
		this.userName = userName;
		this.userID = userID;
	}
	
	public String getUserName() {
		return userName;
	}

	public int getUserID() {
		return userID;
	}
	
	public HashMap<String, Double> getRatedMovies(){
		return movieRatings;
	}
	
	public void addRatedMovie(String title, double rating) {
		movieRatings.put(title, rating);
	}
	
	public boolean equals(UserEntity other){
		if(this == other || other.getUserID() == this.userID) {
			return true;
		}
		return false;
	}
}
