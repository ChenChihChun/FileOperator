package com.jcs.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JCSFileUtils {

	private String urlUploadPath = "";
	private String urlDownloadPath ="";
	private String dbPassword = "";
	private String dbAccount = "";
	private String dbUrl = "";
	
	public String getUrlUploadPath() {
		return this.urlUploadPath;
	}
	public String getUrlDownloadPath() {
		return this.urlDownloadPath;
	}
	public String getDbPassword() {
		return this.dbPassword;
	}
	public String getDbAccount() {
		return this.dbAccount;
	}
	public String getDbUrl() {
		return this.dbUrl;
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
		this.urlUploadPath = prop.getProperty("urlUploadPath");
		this.urlDownloadPath = prop.getProperty("urlDownloadPath");
		this.dbAccount = prop.getProperty("dbAccount");
		this.dbPassword = prop.getProperty("dbPassword");
		this.dbUrl =  prop.getProperty("dbUrl");
	}

}
