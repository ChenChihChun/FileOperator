package com.jcs.FileOperator;

import java.net.URLEncoder;
import java.util.Base64;

import javax.crypto.SecretKey;

import com.jcs.util.JCSEncrypt;

public class TestDonwload {

	public static void  main(String[] args) {
		FileDownloadByHttp download = new FileDownloadByHttp();
		
		try {
			String requestPath = "C:\\MOEAFILE\\PDFMerge\\Finishing\\106\\10603024230\\1060302423d0.pdf";
			String outpathPath = "C:\\\\MOEAFILE_O\\\\PDFMerge\\\\Finishing\\\\106\\\\10603024230\\\\10603024230.pdf";

			JCSEncrypt encrypt = new JCSEncrypt();
			SecretKey secretKey = encrypt.secretKeyGenerator();
			byte[] iv = encrypt.ivGenerator();
			String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			String base64iv = Base64.getEncoder().encodeToString(iv);
			String base64EncodeData = encrypt.encryptData(secretKey, iv);
			
			switch(download.downloadFile(requestPath, outpathPath,encodedKey,base64EncodeData,base64iv)) {
				default:
					System.out.println("download Success!!");
					break;
				case 406:
					System.out.println("Not allow file to download!!(maybe not exist or path not allow)");
					break;
				case 400:
					System.out.println("request parameter error");
					break;
				case 500:
					System.out.println(String.format("%s download Failed!!",requestPath));
					break;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//only in client
			System.exit(0);
		}
		
	}
}
