package com.jcs.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JCSFileUtils {

	private String urlPath = "";
	
	public String getUrlPath() {
		return this.urlPath;
	}
	
	public static boolean isTextFile(File file) {
		return file.getName().toLowerCase().endsWith(".txt");
	}
	
	public JCSFileUtils() throws IOException {
		Properties prop = new Properties();
		String filename = "disp.properties";
		InputStream input = JCSFileUtils.class.getClassLoader().getResourceAsStream(filename);
		if(input==null){
		    return;
		}
		//load a properties file from class path, inside static method
		prop.load(input);
		this.urlPath = prop.getProperty("urlPath");
	}

}
