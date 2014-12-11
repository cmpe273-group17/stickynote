package com.sjsu.sticky.sso.svc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

@Component
public class NtesServiceImpl implements NotesService {

	public void addNote(String newNotes, String authCode) throws Exception {
		// Get your app key and secret from the Dropbox developers website.
		final String APP_KEY = "jpukeu8ca9bxvfv";
		final String APP_SECRET = "70jsur4veot8gb3";

		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
				Locale.getDefault().toString());

		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

		/*
		 * // Have the user sign in and authorize your app. String authorizeUrl
		 * = webAuth.start(); DbxAuthFinish authFinish =
		 * webAuth.finish(authCode);
		 */
		String accessToken = "vdTx5hUt1D0AAAAAAAAAQ-p5CW453q_rWj_ye5K7DSI";

		DbxClient client = new DbxClient(config, accessToken);

		System.out.println("Linked account: "
				+ client.getAccountInfo().displayName);

		File inputFile = new File("working-draft.txt");

		inputFile.createNewFile();
		FileInputStream inputStream = new FileInputStream("working-draft.txt");
		FileWriter fileWriter = new FileWriter(inputFile);
		fileWriter.write(newNotes);
		fileWriter.flush();

		try {
			DbxEntry.File uploadedFile = client.uploadFile(
					"/note-" + System.currentTimeMillis() + ".txt",
					DbxWriteMode.add(), inputFile.length(), inputStream);
			System.out.println("Uploaded: " + uploadedFile.toString());
		} finally {
			inputStream.close();
		}

		DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
		System.out.println("Files in the root path:");
		for (DbxEntry child : listing.children) {
			System.out.println("	" + child.name + ": " + child.toString());
		}

		FileOutputStream outputStream = new FileOutputStream("magnum-opus.txt");
		try {
			DbxEntry.File downloadedFile = client.getFile("/magnum-opus.txt",
					null, outputStream);
			System.out.println("Metadata: " + downloadedFile.toString());
		} finally {
			outputStream.close();
		}
	}

}
