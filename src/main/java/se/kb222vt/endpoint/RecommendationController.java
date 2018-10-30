package se.kb222vt.endpoint;

import static spark.Spark.get;

import com.google.gson.Gson;

import se.kb222vt.logic.RecommendationLogic;
import spark.Request;
import spark.Response;
import spark.Route;

public class RecommendationController {
	static RecommendationLogic logic = new RecommendationLogic();
	static Gson gson = new Gson();

    public static Route pearson = (Request request, Response response) -> {
    	int userID = Integer.parseInt(request.params("userID"));
    	return gson.toJson(logic.userRecPearson(userID));
    };
    public static Route euclidean = (Request request, Response response) -> {
    	int userID = Integer.parseInt(request.params("userID"));
    	return gson.toJson(logic.userRecEuclidean(userID));
    };
        
      
}