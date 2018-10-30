package se.kb222vt.endpoint;

import com.google.gson.Gson;

import se.kb222vt.app.Application;
import se.kb222vt.entities.UserEntity;
import se.kb222vt.logic.RecommendationLogic;
import spark.Request;
import spark.Response;
import spark.Route;

public class RecommendationController {
	static RecommendationLogic logic = new RecommendationLogic();
	static Gson gson = new Gson();

    public static Route pearson = (Request request, Response response) -> {
    	int userID = Integer.parseInt(request.params("userID"));
		UserEntity user = Application.getUsers().get(userID);
		if(user == null) {
			//TODO: implement error
		}
    	return gson.toJson(logic.userRecPearson(user));
    };
    public static Route euclidean = (Request request, Response response) -> {
    	int userID = Integer.parseInt(request.params("userID"));
		UserEntity user = Application.getUsers().get(userID);
		if(user == null) {
			throw new IllegalArgumentException("No user found for ID: " + userID);
		}
    	return gson.toJson(logic.userRecEuclidean(user));
    };
        
      
}