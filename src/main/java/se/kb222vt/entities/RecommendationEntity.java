package se.kb222vt.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecommendationEntity {
	private UserEntity recommendationFor; //recommendation for this user
	private SortedMap<Double, UserEntity> similarUsers = new TreeMap<>(); //similarScore, User, sorted descending
	private ArrayList<MovieEntity> recommendedMovies = new ArrayList<>();
	
	public RecommendationEntity(UserEntity user) {
		this.recommendationFor = user;
	}

	public SortedMap<Double, UserEntity> getSimilarUsers() {
		return similarUsers;
	}

	public void setSimilarUsers(SortedMap<Double, UserEntity> similarUsers) {
		this.similarUsers = similarUsers;
	}

	public ArrayList<MovieEntity> getRecommendedMovies() {
		return recommendedMovies;
	}

	public void setRecommendedMovies(ArrayList<MovieEntity> recommendedMovies) {
		this.recommendedMovies = recommendedMovies;
	}

	public UserEntity getRecommendationFor() {
		return recommendationFor;
	}
	
}
