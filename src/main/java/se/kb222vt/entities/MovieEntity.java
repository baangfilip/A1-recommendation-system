package se.kb222vt.entities;

import java.util.HashMap;

public class MovieEntity {
	private String title = "";
	private HashMap<Integer, Double> userRatings = new HashMap<>();
	
	public MovieEntity(String title) {
		this.title = title;
	}

	public String getTitle(){
		return this.title;
	}
	
	public void addUserRating(int userID, double rating) {
		userRatings.put(userID, rating);
	}
	
	public boolean equals(MovieEntity other){
		if(this == other || other.getTitle().equals(this.title)) {
			return true;
		}
		return false;
	}
	
}
