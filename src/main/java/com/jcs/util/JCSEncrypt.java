package com.jcs.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class JCSEncrypt {
	
	private String type = "";
	private final String charset = "UTF-8";
	private String endText = "";
	public JCSEncrypt() {
		this.type = getValue("http_certificate_type");
		this.endText = getValue("http_certificate_endText");
	}
	
	private String getValue(String parameter) {
		OracleConnection conn = null; 
		Connection con =  null;
		String value = "";
		try {
			conn = new OracleConnection();
			con = conn.getConneciton();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select http_value from httpauthdata where http_parameter = '" + parameter + "'");
			while(rs.next()) {
				value = rs.getString(1);
			}
			rs.close();
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (con != null) {
				try {
				con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
		return value;
	}
	
	public byte[] ivGenerator() {
		byte[] iv = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		SecureRandom prng = new SecureRandom();
		prng.nextBytes(iv);
		return iv;
	}
	
	public SecretKey secretKeyGenerator() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		//AES的加密 Secret Key 的長度建議至少要 256 bit 以上，用同一把 Secret Key 做加密時候，應該都要產生新的 IV 來加密
		keyGen.init(256,new SecureRandom() );
		SecretKey secretKey = keyGen.generateKey();
		return secretKey;
		//String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}
	
	
	/***
	 * **must be use already output secretKey and iv to encrypt
	 * @param secretKey
	 * @param iv
	 * @return base64 string
	 * @throws Exception
	 */
	public String encryptData(SecretKey secretKey,byte[] iv) throws Exception {
		Date date = new Date();
		String plainText = "";
		SimpleDateFormat sdf = null;
		if ("date".equals(type)) {
			sdf = new SimpleDateFormat("yyyy/MM/dd");
		} else if ("month".equals(type)) {
			sdf = new SimpleDateFormat("yyyy/MM");
		}
		plainText = Base64.getEncoder().encodeToString(sdf.format(date).concat(endText).getBytes(charset));
		return encrypt(secretKey, iv, plainText);
	}
	
	private String encrypt(SecretKey secretKey, byte[] iv, String msg) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
		// System.out.println("AES_CBC_PKCS5PADDING IV:" + cipher.getIV());
		// System.out.println("AES_CBC_PKCS5PADDING Algoritm:" + cipher.getAlgorithm());
		byte[] byteCipherText = cipher.doFinal(msg.getBytes(charset));
		return Base64.getEncoder().encodeToString(byteCipherText);
	}
	

}
