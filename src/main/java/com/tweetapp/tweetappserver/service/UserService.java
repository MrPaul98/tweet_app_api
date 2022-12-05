package com.tweetapp.tweetappserver.service;

import com.tweetapp.tweetappserver.models.ChangePassword;
import com.tweetapp.tweetappserver.models.LoginUser;
import com.tweetapp.tweetappserver.models.Users;
import com.tweetapp.tweetappserver.response.UserResponse;

/**
 * @author cogjava3341
 *
 */
public interface UserService {
	
	/**
	 * @param users
	 * @return
	 */
	public UserResponse createUser(Users users);
	
	/**
	 * @param loginUser
	 * @return
	 */
	public UserResponse loginUser(LoginUser loginUser);
	
	/**
	 * @param user
	 * @return
	 */
	public UserResponse logoutUser(String user);
	
	/**
	 * @return
	 */
	public UserResponse allUsers();
	
	/**
	 * @param username
	 * @return
	 */
	public UserResponse searchUserWithUserName(String username);
	
	/**
	 * @param username
	 * @return
	 */
	public boolean sendMail(String username);
	
	/**
	 * @param changePassword
	 * @return
	 */
	public UserResponse changePassword(ChangePassword changePassword );

}
