package se.kb222vt.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import se.kb222vt.app.Application;
import se.kb222vt.entities.MovieEntity;
import se.kb222vt.entities.RecommendationEntity;
import se.kb222vt.entities.UserEntity;

public class RecommendationLogic {
	
	public RecommendationEntity userRecEuclidean(int userID) {
		UserEntity user = Application.getUsers().get(userID);
		SortedMap<Double, UserEntity> similarUsers = findSimilarUsersEuclidean(user);
		//got similar persons and their similarity
		//now --> find out the weighted score on movies by person (for movies user haven't watched already)
		//then --> sum the weighted scores for each movie and divide by the sum of similarity
		RecommendationEntity rec = new RecommendationEntity(user);
		rec.setRecommendedMovies(new ArrayList<MovieEntity>(Application.getMovies().values()));
		rec.setSimilarUsers(similarUsers);
		return rec;
	}
	
	public RecommendationEntity userRecPearson(int userID) {
		UserEntity user = Application.getUsers().get(userID);
		RecommendationEntity rec = new RecommendationEntity(user);
		rec.setRecommendedMovies(new ArrayList<MovieEntity>(Application.getMovies().values()));
		rec.setSimilarUsers(new TreeMap<Double, UserEntity>());
		return rec;
	}	
	
	/**
	 * Find similar users by calculating euclidean distance between other users that have watched the same movies
	 * @param user find similar users to user
	 * @return a SortedMap with users sorted descending, the most similar user first
	 */
	private SortedMap<Double, UserEntity> findSimilarUsersEuclidean(UserEntity user) {
		SortedMap<Double, UserEntity> similarUsers = new TreeMap<Double, UserEntity>().descendingMap();
		for(UserEntity u : Application.getUsers().values()) {
			if(u.equals(user))
				continue;
			double similarity = euclideanDistanceUsers(user, u);
			System.out.println(user.getUserName() + " is " + similarity + "s to: " + u.getUserName());
			if(similarity > 0) {
				similarUsers.put(similarity, u);
			}
		}
		return similarUsers;
	}
	/**
	 * Compare two users and the ratings the gave movies that they both watched with euclidean distance
	 * @param user
	 * @param anotherUser
	 * @return the sum of the euclidean distance of their ratings on the same movies
	 */
	private double euclideanDistanceUsers(UserEntity user, UserEntity anotherUser) {
		double similarity = 0;
		int matchingMovies = 0;
		for(Map.Entry<MovieEntity, Double> movieRating : user.getRatedMovies().entrySet()) {
			
			for(Map.Entry<MovieEntity, Double> movieRatingOther : anotherUser.getRatedMovies().entrySet()) {
				if(movieRating.getKey().equals(movieRatingOther.getKey())) {
					System.out.println("Both " + user.getUserName() + " and " + anotherUser.getUserName() + " have watched " + movieRatingOther.getKey().getTitle());
					similarity += Math.pow(movieRating.getValue() - movieRatingOther.getValue(), 2);
					matchingMovies++;
				}
			}
		}
		if(matchingMovies < 1) {
			return 0;
		}
		return 1 / (1 + similarity);
	}
	
}
