package se.kb222vt.entities;

import java.util.HashMap;

public class MovieEntity {
	private String title = "";
	private HashMap<Integer, Double> userRatings = new HashMap<>();
	
	public MovieEntity(String title) {
		this.setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addUserRating(UserEntity user, double rating) {
		System.out.println("User: " + user.getUserName() + " rated: " + rating);
		userRatings.put(user.getUserID(), rating);
	}
	
}
