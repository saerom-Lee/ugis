package ugis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Component;

@Component
public class HttpUploader {

	private static final String LINE_FEED = "\r\n";
	private static String charset = "euc-kr";
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	private static OutputStream outputStream;
	private static PrintWriter writer;
	private static String boundary;

	public boolean upload(String boundary, String urlStr, String uploadPath, String filePath) {

		try {
			this.boundary = boundary;
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println("boundary : " + boundary);
			System.out.println("urlStr : " + urlStr);
			System.out.println("uploadPath : " + uploadPath);
			System.out.println("filePath : " + filePath);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");

			File file = new File(filePath);
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Content-Type",
					"multipart/form-data;charset=" + charset + ";boundary=" + boundary);
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setChunkedStreamingMode(DEFAULT_BUFFER_SIZE);
			connection.setConnectTimeout(999999999);
			outputStream = connection.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
			addFilePart("file", file);
			addTextPart("filepath", uploadPath);
//			addTextPart("type", "NORMAL"); // geoserver 미발행
			addTextPart("type", "NLIP"); // geoserver 발행
			// addTextPart("osType", copy_os_type);
			writer.append("--" + boundary + "--").append(LINE_FEED);
			writer.close();
			connection.disconnect();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
//		try {
//			// Set header
//			URL url = new URL(urlStr);
//			connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("Content-Type",
//					"multipart/form-data;charset=" + charset + ";boundary=" + boundary);
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//			connection.setUseCaches(false);
//			connection.setConnectTimeout(10000);
//
//			int responseCode = connection.getResponseCode();
//			System.out.println(">>>>>>>>>>>>>>>>>> upload boundary : " + boundary);
//			System.out.println(">>>>>>>>>>>>>>>>>> upload urlStr : " + urlStr);
//			System.out.println(">>>>>>>>>>>>>>>>>> upload uploadPath : " + uploadPath);
//			System.out.println(">>>>>>>>>>>>>>>>>> upload filePath : " + filePath);
//			System.out.println(">>>>>>>>>>>>>>>>>> upload connect : " + responseCode);
//			// if (responseCode == 200) {
//			outputStream = connection.getOutputStream();
//			writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
//
//			System.out.println(">>>>>>>>>>>>>>>>>> upload file exists1 : " + new File(filePath).exists());
//
//			// Add file
//			addFilePart(boundary, "file", new File(filePath));
//
//			System.out.println(">>>>>>>>>>>>>>>>>> upload file exists2 : " + new File(filePath).exists());
//
//			// Add form field
//			addFormField(boundary, "filepath", uploadPath);
//			addFormField(boundary, "type", "NORMAL"); // geoserver 미발행
////			addFormField(boundary, "type", "NLIP"); // geoserver 발행
//
//			String response = finish(boundary);
//			System.out.println("upload RESPONSE: " + response);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
	}

	/**
	 * TRANSFER URLConnection ADD TEXT PARAM
	 * 
	 * @param name
	 * @param value
	 * @throws IOException
	 */
	public static void addTextPart(String name, String value) throws Exception {
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
		writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.append(value).append(LINE_FEED);
		writer.flush();
	}

	/**
	 * TRANSFER URLConnection ADD FILE PARAM
	 * 
	 * @param name
	 * @param file
	 * @throws IOException
	 */
	public static void addFilePart(String name, File file) throws Exception {
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getName() + "\"")
				.append(LINE_FEED);
		writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();

		FileInputStream inputStream = new FileInputStream(file);
		copyLarge(inputStream, outputStream);
		outputStream.flush();
		inputStream.close();
		writer.append(LINE_FEED);
		writer.flush();
	}

	public static long copyLarge(InputStream input, OutputStream output) throws Exception {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		// System.out.println(count);
		while (-1 != (n = input.read(buffer))) {
			// System.out.println(count);
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

//	/**
//	 * Adds a form field to the request
//	 *
//	 * @param name  field name
//	 * @param value field value
//	 */
//	public void addFormField(String boundary, String name, String value) {
//		writer.append("--" + boundary).append(LINE);
//		writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE);
//		writer.append("Content-Type: text/plain; charset=" + charset).append(LINE);
//		writer.append(LINE);
//		writer.append(value).append(LINE);
//		writer.flush();
//	}
//
//	/**
//	 * Adds a upload file section to the request
//	 *
//	 * @param fieldName
//	 * @param uploadFile
//	 * @throws IOException
//	 */
//	public void addFilePart(String boundary, String fieldName, File uploadFile) throws IOException {
//		String fileName = uploadFile.getName();
//		writer.append("--" + boundary).append(LINE);
//		writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"")
//				.append(LINE);
//		writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE);
//		writer.append("Content-Transfer-Encoding: binary").append(LINE);
//		writer.append(LINE);
//		writer.flush();
//
//		FileInputStream inputStream = new FileInputStream(uploadFile);
//		byte[] buffer = new byte[4096];
//		int bytesRead = -1;
//		while ((bytesRead = inputStream.read(buffer)) != -1) {
//			outputStream.write(buffer, 0, bytesRead);
//		}
//		outputStream.flush();
//		inputStream.close();
//		writer.append(LINE);
//		writer.flush();
//	}
//
//	/**
//	 * Completes the request and receives response from the server.
//	 *
//	 * @return String as response in case the server returned status OK, otherwise
//	 *         an exception is thrown.
//	 * @throws IOException
//	 */
//	public String finish(String boundary) throws IOException {
//		String response = "";
//		writer.flush();
//		writer.append("--" + boundary + "--").append(LINE);
//		writer.close();
//
//		// checks server's status code first
//		int status = connection.getResponseCode();
//		if (status == HttpURLConnection.HTTP_OK) {
//			ByteArrayOutputStream result = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int length;
//			while ((length = connection.getInputStream().read(buffer)) != -1) {
//				result.write(buffer, 0, length);
//			}
//			response = result.toString(this.charset);
//			connection.disconnect();
//		} else {
//			throw new IOException("Server returned non-OK status: " + status);
//		}
//		return response;
//	}

}
