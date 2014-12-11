package com.sjsu.sticky.sso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;

public class AccessToken {

	private String appKey;
	private String appSecret;

	public AccessToken(String appKey, String appSecret) {
		this.appKey = appKey;
		this.appSecret = appSecret;

	}

	public AccessTokenPair request() throws DropboxException, IOException {

		AppKeyPair appKeys = new AppKeyPair(this.appKey, this.appSecret);
		WebAuthSession webSession = new WebAuthSession(appKeys,
				AccessType.APP_FOLDER);
		WebAuthInfo webAuthInfo = webSession.getAuthInfo();

		RequestTokenPair reqTokenPair = webAuthInfo.requestTokenPair;
		String url = webAuthInfo.url;

		System.out
				.println("Accept the app by openning your browser at this URL:");
		System.out.println(url);
		System.out.println("THEN press any key:");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		br.readLine();

		try {
			webSession.retrieveWebAccessToken(reqTokenPair);
		} catch (Exception e) {
			System.out.println("authentication fail with exception:" + e);
		}

		AccessTokenPair accessTokenPair = webSession.getAccessTokenPair();
		return accessTokenPair;

	}

	private String getPersistantFilePath() {

		String dir = System.getProperty("user.home")
				+ System.getProperty("file.separator");
		String filePath = dir + ".dir2Server";
		return filePath;

	}

	public void saveDefaultToken(AccessTokenPair token) throws IOException {

		String filePath = getPersistantFilePath();
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(
				file.getAbsoluteFile()));
		bw.write(token.key);
		bw.newLine();
		bw.write(token.secret);
		bw.newLine();
		bw.close();
	}

	public AccessTokenPair getDefaultToken() {
		String filePath = getPersistantFilePath();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String key = br.readLine();
			String secret = br.readLine();

			return new AccessTokenPair(key, secret);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

}
