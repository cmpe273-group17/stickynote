package com.sjsu.sticky.sso.svc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Component;

@Component
public class DbxAuthService {

	private String code;
	private String grant_type = "authorization_code";
	private String client_id = "jpukeu8ca9bxvfv";
	private String client_secret = "70jsur4veot8gb3";
	private String redirect_uri = "http://localhost:8080/SSO/api/login";

	public String getAuthToken(String code) throws IOException {
		String url = "https://api.dropbox.com/1/oauth2/token";
		this.code = code;
		URL urlObj = new URL(url);

		HttpsURLConnection httpConn = (HttpsURLConnection) urlObj.openConnection();

		// add request header
		httpConn.setRequestMethod("POST");
		// httpConn.setRequestProperty("User-Agent", USER_AGENT);
		httpConn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		String urlParameter = "code=" + this.code + "&grant_type=" + grant_type + "&client_id=" + client_id + "&client_secret=" + client_secret
				+ "&redirect_uri="+redirect_uri;
		// + "&redirect_uri=" + redirect_uri;

		// Send post request
		httpConn.setDoOutput(true);
		DataOutputStream dataoutputStream = new DataOutputStream(httpConn.getOutputStream());
		dataoutputStream.writeBytes(urlParameter);
		dataoutputStream.flush();

		int responseCode = httpConn.getResponseCode();
		System.out.println("Response Code : " + responseCode);
		StringBuffer response = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			dataoutputStream.close();
			// print result
			System.out.println(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();
	}

	public String Authorized(String oauth_token, String uid) {

		return null;
	}

}
