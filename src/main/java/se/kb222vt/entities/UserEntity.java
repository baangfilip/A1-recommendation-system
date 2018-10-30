package se.kb222vt.entities;

import java.util.ArrayList;
import java.util.HashMap;

public class UserEntity {
	private String userName = "";
	private int userID;
	private HashMap<MovieEntity, Double> movieRatings = new HashMap<>();
	
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
	
	public HashMap<MovieEntity, Double> getRatedMovies(){
		return movieRatings;
	}
	
	public void addRatedMovie(MovieEntity movie, double rating) {
		movieRatings.put(movie, rating);
	}
	
	public boolean equals(UserEntity other){
		if(this == other || other.getUserID() == this.userID) {
			return true;
		}
		return false;
	}
}
