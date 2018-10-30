package se.kb222vt.app;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.port;

public class Initalize {
	//Initalize from Eclipse
    public static void main(String[] args) {
		externalStaticFileLocation("src/main/webapp");
		port(8080);
    	Application app = new Application();
    	app.setUsersCSV("src/main/resources/data/movies/users.csv");
    	app.setMoviesCSV("src/main/resources/data/movies/ratings.csv");
    	app.init();
    }
}