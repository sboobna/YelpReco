package org.bigdata.yelp.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
	
	static Properties prop;
		
	public static Properties getPropInstance() throws ClassNotFoundException, IOException{
		if(prop == null) {
			prop = new Properties();
			String filePath = "/config.properties";
			InputStream inputStream = Class.class.getResourceAsStream(filePath);
			if(inputStream == null) {
				throw new FileNotFoundException("Config file not found at " + filePath);
			}
			prop.load(inputStream);
			
		}		
		return prop;
	}
	
}
