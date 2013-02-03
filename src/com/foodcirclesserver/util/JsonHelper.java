package com.foodcirclesserver.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class JsonHelper {
	
	public static String getJSONfromUrl(String inputURL) {

			StringBuilder builder = new StringBuilder();

			URL url;
			try {
				url = new URL(inputURL);


				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String line; 

				while((line = reader.readLine()) != null) {
					builder.append(line);
				}

				reader.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


			return builder.toString();
		}

}
