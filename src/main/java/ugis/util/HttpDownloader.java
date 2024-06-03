package ugis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import org.springframework.stereotype.Component;

@Component
public class HttpDownloader {

	private final static String LINE_FEED = "\r\n";
	private final static String charset = "euc-kr";
	private static OutputStream outputStream;
	private static PrintWriter writer;

	// downloadPath : 로컬경로, filePath : 원격경로
	public boolean download(String boundary, String urlStr, String downloadPath, String filePath) {
		try {
			int responseCode;
			InputStream is = null;
			FileOutputStream os = null;

			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;charset=" + charset + ";boundary=" + boundary);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(10000);
			outputStream = connection.getOutputStream();

			writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
			addTextPart(boundary, "filepath", filePath);
			writer.append("--" + boundary + "--").append(LINE_FEED);
			writer.close();

			responseCode = connection.getResponseCode();
			System.out.println(responseCode + ">>>>>>>>>>>>>>");

			System.out.println("boundary = " + boundary);
			System.out.println("url = " + urlStr);
			System.out.println("downloadPath = " + downloadPath);
			System.out.println("filePath = " + filePath);

			if (responseCode == 200) {
				String fileName = "";
				String disposition = connection.getHeaderField("Content-Disposition");
				String contentType = connection.getContentType();

				// 일반적으로 Content-Disposition 헤더에 있지만
				// 없을 경우 url 에서 추출해 내면 된다.
				if (disposition != null) {
					String target = "filename=";
					int index = disposition.indexOf(target);
					if (index != -1) {
						fileName = disposition.substring(index + 10, disposition.length() - 1);
					}
				}

				System.out.println("Content-Type = " + contentType);
				System.out.println("Content-Disposition = " + disposition);
				System.out.println("fileName = " + fileName);

				File downloadFile = new File(downloadPath);
				File outDir = downloadFile.getParentFile();
				if (!outDir.exists()) {
					outDir.mkdirs();
				}

				String outFName = downloadFile.getName();
				is = connection.getInputStream();
				os = new FileOutputStream(new File(outDir, outFName));

				final int BUFFER_SIZE = 4096;
				int bytesRead;
				byte[] buffer = new byte[BUFFER_SIZE];
				while ((bytesRead = is.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				is.close();
				System.out.println("download SUCCESS HTTP code: " + responseCode);
			} else {
				System.out.println("download ERROR HTTP code: " + responseCode);
			}
			connection.disconnect();
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	/**
	 * TRANSFER URLConnection ADD TEXT PARAM
	 * 
	 * @param name
	 * @param value
	 * @throws IOException
	 */
	public static void addTextPart(String boundary, String name, String value) throws IOException {
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
	public static void addFilePart(String boundary, String name, File file) throws IOException {
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getName() + "\"")
				.append(LINE_FEED);
		writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();

		FileInputStream inputStream = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.flush();
		inputStream.close();
		writer.append(LINE_FEED);
		writer.flush();
	}

	public static void downloadToFile(URL url, File savedFile) throws IOException {
		if (url == null)
			throw new IllegalArgumentException("url is null.");
		if (savedFile == null)
			throw new IllegalArgumentException("savedFile is null.");
		if (savedFile.isDirectory())
			throw new IllegalArgumentException("savedFile is a directory.");
		downloadTo(url, savedFile, false);
	}

	public static void downloadToDir(URL url, File dir) throws IOException {
		if (url == null)
			throw new IllegalArgumentException("url is null.");
		if (dir == null)
			throw new IllegalArgumentException("directory is null.");
		if (!dir.exists())
			throw new IllegalArgumentException("directory is not existed.");
		if (!dir.isDirectory())
			throw new IllegalArgumentException("directory is not a directory.");
		downloadTo(url, dir, true);
	}

	private static void downloadTo(URL url, File targetFile, boolean isDirectory) throws IOException {

		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			File saveFilePath = null;

			if (isDirectory) {
				if (disposition != null) {
					int index = disposition.indexOf("filename=");
					if (index > 0) {
						fileName = disposition.substring(index + 10, disposition.length() - 1);
					}
				} else {
					String fileURL = url.toString();
					fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
					int questionIdx = fileName.indexOf("?");
					if (questionIdx >= 0) {
						fileName = fileName.substring(0, questionIdx);
					}
					fileName = URLDecoder.decode(fileName);
				}
				saveFilePath = new File(targetFile, fileName);
			} else {
				saveFilePath = targetFile;
			}

			InputStream inputStream = httpConn.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);
			int bytesRead = -1;
			byte[] buffer = new byte[4096];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.close();
			inputStream.close();
			System.out.println("File downloaded to " + saveFilePath);
		} else {
			System.err.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
	}
}