package com.jcs.FileOperator;
import java.io.File;
import java.io.IOException;

import com.jcs.FileOperator.FileUploadByHttp;

public class Test {

	//folder complete copy to server 
	public static void  main (String[] args) {
		try {
			FileUploadByHttp test = new FileUploadByHttp();

			for(File file : new File("C:\\wildfly-10.1.0.Final\\docs\\licenses").listFiles()) {
				String parentFolder = file.getParent();
				String[] param = new String[] {"ac","pd","uploadPath"};
				String[] value = new String[] {"jcs","jcsRU*T?^",parentFolder.substring(3,parentFolder.length())};
				
				switch (test.sendFile(file, param, value)) {
					default:
						System.out.println("Upload Success!!");
						break;
					case 406:
						System.out.println("account or password  is wrong !!");
						break;
					case 400:
						System.out.println("No File Upload!!");
						break;
					case 413:
						System.out.println(String.format("%s File to Big, Upload Failed!!",file.getAbsolutePath()));
						break;
					case 500:
						System.out.println(String.format("%s Upload Failed!!",file.getAbsolutePath()));
						break;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
