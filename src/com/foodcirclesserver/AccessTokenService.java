package com.foodcirclesserver;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


public class AccessTokenService {
	
	private static final String UID = "user_id";
	private static final String ACCESS = "access_token";
	private static final String EXP = "expires";
	private static final String TYPE = "fb_acess";
	
	public static void addToken(String userID, String accessToken, String expires) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Entity user = new Entity(TYPE, userID);
		user.setProperty(UID, userID);
		user.setProperty(ACCESS, accessToken);
		user.setProperty(EXP, expires);
		
		ds.put(user);
	}
	
	public static AccessToken getToken(String userID) throws EntityNotFoundException {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		 Key k = KeyFactory.createKey(TYPE, userID); //use userID to create key
		 
		Entity user = ds.get(k);
		
		String access = (String) user.getProperty(ACCESS);
		String exp = (String) user.getProperty(EXP);
		return new AccessToken(userID, access, exp);
	}

}
