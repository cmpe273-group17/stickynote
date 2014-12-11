package com.sjsu.sticky.sso;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sjsu.edu.cmpe273.dao.DropBoxDAO;
import com.sjsu.edu.cmpe273.dao.UserDAO;
import com.sjsu.edu.cmpe273.mail.agent.GoogleMailAgent;
import com.sjsu.edu.cmpe273.mail.agent.MailAgent;
import com.sjsu.edu.cmpe273.vo.Document;
import com.sjsu.edu.cmpe273.vo.User;
import com.sjsu.sticky.sso.svc.DbxAuthService;

@RestController
public class CreateNoteController {
	private static final String DROPBOX_AUTH_TOKEN = "dropbox_auth_token";
	private static final Logger LOG = Logger
			.getLogger(CreateNoteController.class);

	DbxAuthService dbxAuthService = new DbxAuthService();
	UserDAO userDAO = UserDAO.getUserDAO();
	DropBoxDAO dropBoxDAO = new DropBoxDAO();

	// 1. Create cookie
	@RequestMapping(value = "/login", produces = "application/json", method = RequestMethod.GET)
	public ModelAndView getAuthenticationCookie(@RequestParam String code,
			@RequestParam String state, HttpServletResponse response)
			throws IOException {
		String dbxResponse = dbxAuthService.getAuthToken(code);
		String accessToken = null;
		if (StringUtils.isNotEmpty(dbxResponse)) {
			JsonElement parse = new JsonParser().parse(dbxResponse);
			if (parse.isJsonObject()) {
				accessToken = parse.getAsJsonObject().get("access_token")
						.getAsString();
			}
		}
		LOG.info("Controller running..");
		User user = userDAO.getUserDetails(state);
		userDAO.updateUser(user, accessToken);

		// Send Email notification
		MailAgent mailAgent = new GoogleMailAgent();
		mailAgent.sendRegistrationMail(user.getName(), user.getEmail(), "", "");

		return new ModelAndView("/index.html");

	}

	private String getAuthCodeFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (DROPBOX_AUTH_TOKEN.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}

		return null;
	}

	@RequestMapping(value = "/stickynotes/register", produces = "application/json", method = RequestMethod.POST)
	public User registerUser(@RequestBody User user,
			HttpServletResponse response) throws Exception {
		// Code added by Avadhut Thakar - START
		UserDAO userDao = UserDAO.getUserDAO();
		if (userDao.addUser(user)) {

//			if (userDao
//					.updateUser(user,
//							"G9nEOev857IAAAAAAAAAK9uhYHzAZqaUIyUTW9KL9L4Ml8Lnc1CnpMAGRBNnCAWY")) {
//
//				return user;
//			} else {
//				// Throw error - dropbox auth code storing failed
//				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
//						"The Authcode update failed");
//			}
		} else {
			// Return the error - user creation failed.
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
					"The User registration failed. Please try again later.");
			return null;
		}
		// Code added by Avadhut Thakar - END

		return user;
	}

	@RequestMapping(value = "/stickynotes/register/db/{userId}", produces = "application/json", method = RequestMethod.GET)
	public User registerDropboxCode(@PathVariable User user,
			HttpServletResponse response) throws Exception{
		// Code added by Avadhut Thakar - START
		UserDAO userDao = UserDAO.getUserDAO();
		if (userDao.addUser(user)) {

//			if (userDao
//					.updateUser(user,
//							"G9nEOev857IAAAAAAAAAK9uhYHzAZqaUIyUTW9KL9L4Ml8Lnc1CnpMAGRBNnCAWY")) {
//
//				return user;
//			} else {
//				// Throw error - dropbox auth code storing failed
//				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
//						"The Authcode update failed");
//			}
		} else {
			// Return the error - user creation failed.
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
					"The User registration failed. Please try again later.");
		}
		// Code added by Avadhut Thakar - END
		// Send Email notification
		MailAgent mailAgent = new GoogleMailAgent();
		mailAgent.sendRegistrationMail(user.getName(), user.getEmail(), "", "");
		return user;
	}

	// 3. login ; Author : Prakruthi Nagaraj
	@RequestMapping(value = "/stickynotes/login", produces = "application/json", method = RequestMethod.POST)
	public User loginUser(@RequestBody User user, HttpServletResponse response)
			throws Exception {

		// Code added by Avadhut Thakar - START
		UserDAO userDao = UserDAO.getUserDAO();
		DropBoxDAO dropbBoxDao = new DropBoxDAO();

		if (userDao.validateLogin(user)) {
			dropbBoxDao
					.getAllFiles("{\"access_token\" : \"" + user.getAuthCode()
							+ "\"}", user);
		} else {
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
					"Invalid user id or password entered");

		}
		// Code added by Avadhut Thakar - END

		LOG.info("loggedin.." + user.toString());

		return user;
		// Front End Testing Ends Here

	}

	// 4. View all User Notes ; Author : Prakruthi Nagaraj
	@RequestMapping(value = "/stickynotes/user/{user_id}/viewnotes", produces = "application/json", method = RequestMethod.GET)
	public User viewAllNotes(@PathVariable("user_id") String user_id,
			HttpServletResponse response) throws Exception {
		// String authCodeFromCookie = getAuthCodeFromCookie(request);
		// Code added by Avadhut Thakar - START
		UserDAO userDao = UserDAO.getUserDAO();
		DropBoxDAO dropbBoxDao = new DropBoxDAO();
		User user = userDao.getUserDetails(user_id);

		if (user.getAuthCode() == null
				|| user.getAuthCode().equalsIgnoreCase("")) {
			response.sendError(
					HttpServletResponse.SC_EXPECTATION_FAILED,
					"The Auth code is not present for user. Please delete and create the account again");
			return null;
		}

		// Call only if document list is present
		if (user.getDocumentList() != null && user.getDocumentList().size() > 0) {
			dropbBoxDao
					.getAllFiles("{\"access_token\" : \"" + user.getAuthCode()
							+ "\"}", user);
		}
		LOG.info("Controller running..");

		return user;

	}

	/**
	 * This controller will serve request from customer to delete the note.
	 *
	 * @param user_id
	 * @param document
	 * @return
	 * @throws Exception
	 *
	 *             Author : Prakruthi Nagaraj
	 */
	@RequestMapping(value = "/stickynotes/user/{user_id}/deletenote", method = RequestMethod.DELETE)
	public User deleteNote(@PathVariable("user_id") String user_id,
			@RequestBody Document document, HttpServletResponse response)
			throws Exception {

		// Code added by Avadhut Thakar - START
		UserDAO userDAO = UserDAO.getUserDAO();
		User user = new User();
		user.setUser(user_id);
		userDAO.removeDocument(user_id, document.getName());
		user = userDAO.getUserDetails(user_id);
		if (dropBoxDAO.removeNote("{\"access_token\" : \"" + user.getAuthCode()
				+ "\"}", user, document)) {
			dropBoxDAO
					.getAllFiles("{\"access_token\" : \"" + user.getAuthCode()
							+ "\"}", user);
		} else {
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
					"The file deletion failed");
		}
		; //

		// Code added by Avadhut Thakar - END
		return user;
	}

	/**
	 * This controller will serve request from customer to add the note.
	 *
	 * @param user_id
	 * @param document
	 * @return
	 * @throws Exception
	 *
	 *             Author : Prakruthi Nagaraj
	 */

	@RequestMapping(value = "/stickynotes/user/{user_id}/savenote", method = RequestMethod.PUT)
	public User createNote(@PathVariable("user_id") String user_id,
			@RequestBody Document document, HttpServletResponse response)
			throws Exception {

		// Code added by Avadhut Thakar - START
		UserDAO userDAO = UserDAO.getUserDAO();
		User user = new User();
		System.out.println("Inside note");
		if (userDAO.removeDocument(user_id, document.getName())) {

			if (userDAO.addDocument(user_id, document.getName())) {

				user.setUser(user_id);
				user = userDAO.getUserDetails(user_id);
				DropBoxDAO dropBoxDAO = new DropBoxDAO();
				dropBoxDAO.createNote(
						"{\"access_token\" : \"" + user.getAuthCode() + "\"}",
						user, document); //
				dropBoxDAO.getAllFiles(
						"{\"access_token\" : \"" + user.getAuthCode() + "\"}",
						user);
			} else {
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
						"The file addition failed");
			}
		} else {
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
					"The file addition failed");
		}

		// Code added by Avadhut Thakar - END
		if (user.isAlerts()) {
			MailAgent mailAgent = new GoogleMailAgent();
			mailAgent.sendNotesMail(user.getName(), user.getEmail(), "", "");
		}
		return user;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/stickynotes/user/{user_id}/unlink", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody ModelAndView unlinkDropbox(
			@PathVariable("user_id") String user_id) {
		// The code added by Avadhut Thakar START
		UserDAO userDao = UserDAO.getUserDAO();
		DropBoxDAO dropBoxDao = new DropBoxDAO();
		User user = userDao.getUserDetails(user_id);
		int docSize = 0;
		for (; docSize < user.getDocumentList().size(); docSize++) {
			dropBoxDao.removeAllNote("{\"access_token\" : \"" + user.getAuthCode()
					+ "\"}", user);
		}
		
		//userDao.updateUser(user, "");
		userDao.deleteUser(user_id);
		user.getUser();
		// The code added by Avadhut Thakar END
		return new ModelAndView("homepage.html");

	}

	/**
	 * This method will clear cookie and pass call back to the h
	 * 
	 * @author Avadhut Thakar
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/logOut", method = RequestMethod.POST)
	public @ResponseBody ModelAndView logOutApp(@RequestBody User user) {
		// The code added by Avadhut Thakar START

		return new ModelAndView("homepage.html");

	}

}
