package se.kb222vt.endpoint;

import static spark.Spark.*;
import com.google.gson.Gson;

import se.kb222vt.logic.RecommendationLogic;


public class RestExample {
    public static void main(String[] args) {
    	
    	RecommendationLogic logic = new RecommendationLogic();
    	Gson gson = new Gson();
    	get("/hello/:name", (req, res) -> { 
        	String name = req.params("name");
        	return "Hello " + name;
        });
    	
        get("/user/rec/euclidian", (req, res) -> { 
        	String userName = req.queryParams("userName");
        	return gson.toJson(logic.userRecEuclidian(userName));
        });
        get("/user/rec/pearson", (req, res) -> { 
        	String userName = req.queryParams("userName");
        	return gson.toJson(logic.userRecPearson(userName));
        });
        
    }
}