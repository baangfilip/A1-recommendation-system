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
	
	/**
	 * Get recommendation for movies for user using euclidean or pearson similarity measure. Will not include movies the user has already seen.
	 * @param user to get recommendations for
	 * @return RecommendationEntity
	 * @throws Exception 
	 */
	public RecommendationEntity userRec(UserEntity user, String measure) throws Exception {
		SortedMap<Double, UserEntity> similarUsers = findSimilarUsers(user, measure);
		//got similar persons and their similarity
		SortedMap<Double, MovieEntity> movieRec = getWeightedScoresForUnwatchedMovies(similarUsers, getUnwatchedMovies(user));
		RecommendationEntity rec = new RecommendationEntity(user);
		rec.setRecommendedMovies(movieRec);
		rec.setSimilarUsers(similarUsers);
		return rec;
	}
	
	/**
	 * Compare two users and the ratings the gave movies that they both watched with pearson correlation
	 * @param user
	 * @param anotherUser
	 * @return the sum of the pearson correlation of their ratings on the same movies
	 */
	private double pearsonCorrelationUsers(UserEntity user, UserEntity anotherUser) {
		double sum1 = 0, sum2 = 0, sum1sq = 0, sum2sq = 0, pSum = 0;
		int matchingMovies = 0;
		HashMap<String, Double> anotherUserRatings = anotherUser.getRatedMovies();
		for(Map.Entry<String, Double> entry : user.getRatedMovies().entrySet()) {
			String title = entry.getKey();
			double rating = entry.getValue();
			if(anotherUserRatings.containsKey(title)) { //anotherUser have rated this movie
				sum1 += rating;
				sum2 += anotherUserRatings.get(title);
				sum1sq += Math.pow(rating, 2);
				sum2sq += Math.pow(anotherUserRatings.get(title), 2);
				pSum += rating * anotherUserRatings.get(title);
				matchingMovies++;
			}else {
				continue; //anotherUser haven't rated the same movie as user
			}
		}
		if(matchingMovies < 1) {
			return 0;
		}
		double num = pSum - (sum1 * sum2 / matchingMovies);
		double den = Math.sqrt((sum1sq - Math.pow(sum1, 2) / matchingMovies) * (sum2sq - Math.pow(sum2, 2) / matchingMovies));
		return num/den;
	}
	
	
	/**
	 * Find similar users by calculating pearson or using euclidean distance between other users that have watched the same movies
	 * @param user find similar users to user
	 * @param measure use pearson or euclidean
	 * @return a SortedMap with users sorted descending, the most similar user first
	 * @throws Exception 
	 */
	private SortedMap<Double, UserEntity> findSimilarUsers(UserEntity user, String measure) throws Exception {
		SortedMap<Double, UserEntity> similarUsers = new TreeMap<Double, UserEntity>().descendingMap();
		for(UserEntity u : Application.getUsers().values()) {
			if(u.equals(user))
				continue;
			measure = measure.toLowerCase();
			double similarity = 0;
			switch(measure) {
				case "euclidean":
					similarity = euclideanDistanceUsers(user, u);
					break;
				case "pearson":
					similarity = pearsonCorrelationUsers(user, u);
					break;
				default: 
					throw new Exception("Measure is not supported");
					
			}
			//System.out.println(user.getUserName() + " is " + similarity + "s to: " + u.getUserName());
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
		HashMap<String, Double> anotherUserRatings = anotherUser.getRatedMovies();
		for(Map.Entry<String, Double> entry : user.getRatedMovies().entrySet()) {
			String title = entry.getKey();
			double rating = entry.getValue();
			if(anotherUserRatings.containsKey(title)) { //anotherUser have rated this movie
				double movieRatingOther = anotherUserRatings.get(title);
				//System.out.println("Both " + user.getUserName() + " and " + anotherUser.getUserName() + " have watched " + title);
				similarity += Math.pow(rating - movieRatingOther, 2);
				matchingMovies++;
			}else {
				continue; //anotherUser haven't rated the same movie as user
			}
		}
		if(matchingMovies < 1) {
			return 0;
		}
		return 1 / (1 + similarity);
	}
	
	/**
	 * Get movie recommendations for unwatchedMovies based on similarity by similarUsers.
	 * @param similarUsers
	 * @param unwatchedMovies
	 * @return a sortedMap<"RecommendationScore", MovieEntity> with the highest recommended movie first
	 */
	private SortedMap<Double, MovieEntity> getWeightedScoresForUnwatchedMovies(SortedMap<Double, UserEntity> similarUsers, ArrayList<MovieEntity> unwatchedMovies) {
		HashMap<String, Double> weightedMovies = new HashMap<String, Double>();
		SortedMap<Double, MovieEntity> movieRec = new TreeMap<Double, MovieEntity>().descendingMap();
		//System.out.println("SimilarUsers: " + similarUsers.size());
		//System.out.println("UnwatchedMovies: " + unwatchedMovies.size());
		
		for(MovieEntity movie : unwatchedMovies) {
			double totalSimilarityScoreForMovie = 0;
			//calculate the weighted score for this unwatchedMovie
			String unwatchedMovieTitle = movie.getTitle();
			for(Map.Entry<Double, UserEntity> entry : similarUsers.entrySet()) {
				//have this user watched unwatchedMovieTitle?
				UserEntity similarUser = entry.getValue();
				double similarScore = entry.getKey(); //how similar this user is with other user
				if(similarUser.getRatedMovies().containsKey(unwatchedMovieTitle)) {
					totalSimilarityScoreForMovie += similarScore;
					//the similarUser have watched this movie, calc weightedScore based on user and similarUser similarity
					double weightedRating = similarUser.getRatedMovies().get(unwatchedMovieTitle) * similarScore; //weighted score on this movie based on similarity between users
					
					//add to weightedMovies
					boolean weightedScoreExists = weightedMovies.containsKey(unwatchedMovieTitle);
					double totalWeightedScore = (weightedScoreExists ? weightedMovies.get(unwatchedMovieTitle) : 0) + weightedRating;
					weightedMovies.put(unwatchedMovieTitle, totalWeightedScore); //add or overwrite previous score
				}
			}
			//now we have the sum weightedScores for every movie we could calculate it on, lets divide it by the sum of similarity score
			if(weightedMovies.get(unwatchedMovieTitle) != null) {
				double recommendationScore = weightedMovies.get(unwatchedMovieTitle) / totalSimilarityScoreForMovie;
				movieRec.put(recommendationScore, movie);
			}
		}
		
		return movieRec;
	}
	
	/**
	 * Get which movies user haven't watched
	 * @param user
	 * @return a list of movies the user haven't watched
	 */
	private ArrayList<MovieEntity> getUnwatchedMovies(UserEntity user) {
		ArrayList<MovieEntity> unwatchedMovies = new ArrayList<>();
		for(MovieEntity movie : Application.getMovies().values()) {
			if(user.getRatedMovies().containsKey(movie.getTitle())) {
				continue; //user have already watched this movie
			}else {
				unwatchedMovies.add(movie);
			}
		}
		return unwatchedMovies;
	}
	
}
