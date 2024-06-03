package ugis.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGeoserverAPI {

	public boolean send(String urlStr, String paramStr) {
		try {

			System.out.println(">>>>>>>>>>>> deleted reqest url : " + urlStr);
			System.out.println(">>>>>>>>>>>> deleted reqest paramStr : " + paramStr);

			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			bw.write(paramStr);
			bw.flush();
			bw.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response = in.readLine();
			int reponsecde = connection.getResponseCode();
			System.out.println(">>>>>>>>>>>> deleted response : " + reponsecde);
			System.out.println(">>>>>>>>>>>> deleted response : " + response);
			connection.disconnect();
			if (reponsecde == 200) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
