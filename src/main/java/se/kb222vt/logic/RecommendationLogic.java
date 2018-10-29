package se.kb222vt.logic;

import se.kb222vt.app.Application;
import se.kb222vt.entities.MovieEntity;
import se.kb222vt.entities.UserEntity;

public class RecommendationLogic {
	
	public UserEntity userRecEuclidian(String userName) {
		UserEntity user = Application.getUsers().get(userName);
		return user;
	}
	
	public UserEntity userRecPearson(String userName) {
		UserEntity user = Application.getUsers().get(userName);
		return user;
	}	
}
