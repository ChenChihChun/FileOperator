package com.jcs.FileOperator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.jcs.util.JCSFileUtils;

public class FileDownloadByHttp {

	private final String charset = "UTF-8";
	//server output size  :  128 * 1024; //128K
	private byte[] buffer = new byte[128 * 1024];
	
	/***
	 * 
	 * @param requestPath server file absolute path
	 * @param outputPath client file absolute path
	 * @return http responseCode
	 * @throws ClientProtocolException
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public int downloadFile(String requestPath,String outputPath,
			String encodedKey, String base64EncodeData,String base64iv) throws ClientProtocolException, UnsupportedOperationException, IOException {

		JCSFileUtils utils = new JCSFileUtils();
		String url = utils.getUrlDownloadPath();
		url = url.concat("?path=").concat(URLEncoder.encode(requestPath,charset))
				 .concat("&fileName=").concat(URLEncoder.encode(new File(requestPath).getName(), charset));
		url = url.concat("&encodedKey=").concat(URLEncoder.encode(encodedKey, charset));
		url = url.concat("&base64iv=").concat(URLEncoder.encode(base64iv, charset));
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();

		int responseCode = response.getStatusLine().getStatusCode();

		if (responseCode == 200) {
			InputStream is = entity.getContent();
	
			//create folder
			File clientFile = new File(outputPath);
			if (!clientFile.getParentFile().exists()) {
				clientFile.getParentFile().mkdirs();
			}

			FileOutputStream fos = new FileOutputStream(clientFile);
	
			int inByte;
			while ((inByte = is.read(buffer)) != -1) {
				fos.write(buffer, 0 , inByte);
			}
	
			is.close();
			fos.close();
	
			client.close();
		} 
		return responseCode;
	}
}
