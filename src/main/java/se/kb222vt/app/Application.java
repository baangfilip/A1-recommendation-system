package se.kb222vt.app;

import static spark.Spark.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.gson.Gson;

import se.kb222vt.endpoint.RecommendationController;
import se.kb222vt.entities.MovieEntity;
import se.kb222vt.entities.UserEntity;
import spark.servlet.SparkApplication;


public class Application implements SparkApplication {
	//putting some logic here since it will be so much overhead to put it somewhere else
	private static final HashMap<Integer, UserEntity> users = new HashMap<>();
	private static final HashMap<String, MovieEntity> movies = new HashMap<>();
	
	private Gson gson = new Gson();
	//http://zetcode.com/java/spark/
	@Override
	public void init() {
		System.out.println("Start endpoints");
		externalStaticFileLocation("src/main/resources/public");
		port(8080);
        get("/API/rec/user/euclidean/:userID", RecommendationController.euclidean);
        get("/API/rec/user/pearson/:userID", RecommendationController.pearson);
        get("/API/users", (req, res) -> {
        	return gson.toJson(users);
        });
        get("/API/movies", (req, res) -> {
        	return gson.toJson(movies);
        });
        
        try {
			initUsers();
	        initMovies();
	        System.out.println("Found " + users.values().size() + " users");
	        System.out.println("Found " + movies.values().size() + " movies");
		} catch (IOException e) {
			System.err.println("Couldnt read users or movies");
			e.printStackTrace();
		}
	}
	
	private void initUsers() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/data/movies/users.csv"));
        CSVParser csv = new CSVParser(reader, CSVFormat.newFormat(';').withFirstRecordAsHeader());
        for (CSVRecord line : csv) {
            String userName = line.get(0);
            int userID = Integer.parseInt(line.get(1));
            users.put(userID, new UserEntity(userName, userID));
        }
        csv.close();
	}
	
	private void initMovies() throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/data/movies/ratings.csv"));
        CSVParser csv = new CSVParser(reader, CSVFormat.newFormat((";").toCharArray()[0]).withFirstRecordAsHeader().withQuote('"'));
        for(UserEntity user : users.values())
        	System.out.println(user.getUserID() + " : " + user.getUserName());
        for (CSVRecord line : csv) {
            int userID = Integer.parseInt(line.get(0));
            String title = line.get(1);
            double rating = Double.parseDouble(line.get(2));
            MovieEntity movie = null;
            movie = movies.get(title);
            if(movie == null) {
            	movie =  new MovieEntity(title);
            }
            UserEntity user = users.get(userID);
            user.addRatedMovie(movie.getTitle(), rating);
            System.out.println(user.getUserName() + " rated " + movie.getTitle() + " : " + rating);
            movie.addUserRating(user.getUserID(), rating);
            movies.put(title, movie);
        }
        csv.close();
	}
	
	public static HashMap<String, MovieEntity> getMovies() {
		return movies;
	}
	
	public static HashMap<Integer, UserEntity> getUsers() {
		return users;
	}
	
}