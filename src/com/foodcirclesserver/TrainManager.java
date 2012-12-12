/*
 * trainmanager - table kept of trains w/ id, name, time and location
 * -users have "current_train" field, so query user table to get members of a train
 * 
 * -Derek
 * 
 * NOT USING FOR NOW
 */

/*package com.foodcirclesserver;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;


public class TrainManager {
	
	public static String TYPE = "train";
	public static String TRAIN_ID = "train_id";
	public static String TRAIN_NAME = "train_name";
	public static String TIME = "time";
	public static String LOCATION = "location";
	
	public static Train getTrain(Long trainID, DatastoreService ds) {
		if (trainID == null )
			return null;
		
		Key userKey = KeyFactory.createKey(TYPE, TRAIN_ID);
		
		Entity train;

		try {
			train = ds.get(userKey);
		} catch (EntityNotFoundException e) {
			//error: train does not exist
			return null;
		}
		
		String location = (String) train.getProperty(LOCATION);
		Date time = (Date) train.getProperty(TIME);
		String trainName = (String) train.getProperty(TRAIN_NAME);
		
		Train result = new Train(trainID, trainName, time, location);
		
		Query q = new Query(UserManager.USER).addFilter(UserManager.CURRENT_TRAIN,
								Query.FilterOperator.EQUAL, trainID);
		
		Iterator<Entity> pq = ds.prepare(q).asIterator();
		
		while(pq.hasNext()) {
			Entity e = pq.next();
			String user = (String) e.getProperty(UserManager.USER_ID);

			User u = UserManager.getUser(user, ds);
			if (u != null)
				result.users.add(u);
			
		}
		
		return result;
	}
	
	
	 * -need to generate custom key so returns train w/ key
	 * -if no name is included, creates default
	 
	public static Train createTrain(String trainName, Date time, String location, DatastoreService ds) {
		
		DateFormat df = DateFormat.getTimeInstance();
		if (trainName == null) {
			if (location == null) {
				if (time == null) {
					trainName = "Food Train";
				} else {
					trainName = "Food Train at " + df.format(time);
				}
			} else {
				if (time == null) {
					trainName = location + " Food Train";
				} else {
					trainName = location + " at " + df.format(time);
				}
			}
		}
		
		Entity train;
		
		Key trainKey = KeyFactory.createKey(TYPE, trainName);
		
		try {
			train = ds.get(trainKey);
		} catch (EntityNotFoundException e) {
			train = null;
		}
		
		//use random #s until we get a unique key
		while (train != null && trainKey.getId() != 0) {
			Random r = new Random();
			trainKey = KeyFactory.createKey(TYPE, trainName + r.nextLong());
			try {
				train = ds.get(trainKey);
			} catch (EntityNotFoundException e) {
				train = null;
			}
		}
		
		Long trainID = trainKey.getId();
		train = new Entity(TYPE, trainID);
		train.setProperty(TRAIN_NAME, trainName);
		train.setProperty(LOCATION, location);
		train.setProperty(TIME, time);
		ds.put(train);
		
		Train result = new Train(trainID, trainName, time, location);
		return result;
	}
	
	public static boolean updateTrain(Long trainID, String trainName, Date time, String location, DatastoreService ds) {
		if (trainID == null)
			return false;
		
		//check if train is already in db
		Key userKey = KeyFactory.createKey(TYPE, TRAIN_ID);
		
		Entity train;

		try {
			train = ds.get(userKey);
			//don't overwrite old values w/ null!
			if (trainName != null)
				train.setProperty(TRAIN_NAME, trainName);
			if (time != null)
				train.setProperty(TIME, time);
			if (location != null)
				train.setProperty(LOCATION, location);
			ds.put(train);
			return true;
		} catch (EntityNotFoundException e) {
			return false;
		}
	}

}
*/