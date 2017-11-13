package com.jcs.FileOperator;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.SecretKey;

import com.jcs.FileOperator.FileUploadByHttp;
import com.jcs.util.JCSEncrypt;

public class TestUpload {

	//folder complete copy to server 
	public static void  main (String[] args) throws Exception {
		try {
			FileUploadByHttp test = new FileUploadByHttp();

			JCSEncrypt encrypt = new JCSEncrypt();
			SecretKey secretKey = encrypt.secretKeyGenerator();
			byte[] iv = encrypt.ivGenerator();
			String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			String base64iv = Base64.getEncoder().encodeToString(iv);
			String base64EncodeData = encrypt.encryptData(secretKey, iv);
			
			for(File file : new File("C:\\wildfly-10.1.0.Final\\docs\\licenses").listFiles()) {
				String parentFolder = file.getParent();
				String uploadPath = Base64.getEncoder().encodeToString(
						parentFolder.substring(3,parentFolder.length()).getBytes("UTF-8"));
				String[] param = new String[] {"encodedKey","base64EncodeData","base64iv","uploadPath"};
				String[] value = new String[] {encodedKey,base64EncodeData,base64iv,uploadPath};
				
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
