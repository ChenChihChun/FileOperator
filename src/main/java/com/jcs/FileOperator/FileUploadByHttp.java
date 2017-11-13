package com.jcs.FileOperator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;

import com.jcs.util.JCSFileUtils;

/***
 * File upload
 * 
 * @author Peter
 *
 */
public class FileUploadByHttp {

	private String CRLF = "\r\n"; // Line separator required by multipart/form-data.
	private String charset = "UTF-8";
	private String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
	private JCSFileUtils fileUtils = null;

	public FileUploadByHttp() throws IOException {
		fileUtils = new JCSFileUtils();
	}

	private void buildConnectionData(PrintWriter writer, OutputStream output, File file,String[] param, String[] value)
			throws IOException {
		// Send normal param.
		for (int i = 0; i < param.length; i++) {
			writer.append("--" + boundary).append(CRLF);
			writer.append("Content-Disposition: form-data; name=\""+param[i]+"\"").append(CRLF);
			writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
			writer.append(CRLF).append(URLEncoder.encode(value[i], charset)).append(CRLF).flush();
		}
		// Send text file.
		if (JCSFileUtils.isTextFile(file)) {
			writer.append("--" + boundary).append(CRLF);
			writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + file.getName() + "\"")
					.append(CRLF);
			writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF); // Text file itself must be
																						// saved in this charset!
			writer.append(CRLF).flush();
			Files.copy(file.toPath(), output);
		} else {
			// Send binary file.
			writer.append("--" + boundary).append(CRLF);
			writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + file.getName() + "\"")
					.append(CRLF);
			writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(CRLF);
			writer.append("Content-Transfer-Encoding: binary").append(CRLF);
			writer.append(CRLF).flush();
			Files.copy(file.toPath(), output);
		}
		output.flush(); // Important before continuing with writer!
		writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
		// End of multipart/form-data.
		writer.append("--" + boundary + "--").append(CRLF).flush();
	}

	/***
	 * 
	 * @param file send file
	 * @param param like [account]
	 * @param value llke [jcs]
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public int sendFile(File file, String[] param, String[] value) throws MalformedURLException, IOException {
		URLConnection connection = new URL(fileUtils.getUrlUploadPath()).openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		OutputStream output = connection.getOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
		this.buildConnectionData(writer, output, file, param, value);
		// Request is lazily fired whenever you need to obtain information about response.
		return ((HttpURLConnection) connection).getResponseCode();
		// System.out.println(responseCode); // Should be 200
	}

}
