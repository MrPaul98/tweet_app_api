/**
 * 
 */
package com.tweetapp.tweetappserver.service;

import com.tweetapp.tweetappserver.models.TweetMessage;
import com.tweetapp.tweetappserver.response.UserResponse;

/**
 * @author cogjava3341
 *
 */
public interface TweetService {

	/**
	 * @param message
	 * @param username 
	 * @return
	 */
	public UserResponse postNewTweet(TweetMessage message, String username);
	
	/**
	 * @param message
	 * @return
	 */
	public UserResponse updateTweet(TweetMessage message, Integer twtId);
	
	/**
	 * @param id
	 * @return
	 */
	public UserResponse likeTweet(String username, Integer id);
	
	/**
	 * @param id
	 * @return
	 */
	public UserResponse deleteTweet(Integer id);
	
	/**
	 * @param message
	 * @return
	 */
	public UserResponse replyTweet(TweetMessage message, String username);
	
	/**
	 * @return
	 */
	public UserResponse getAllTweet();
	
	/**
	 * @param id
	 * @return
	 */
	public UserResponse getReplyAllTweet(Integer id);
	
	/**
	 * @param username
	 * @return
	 */
	public UserResponse getAllTweetOfUser(String username);
	
	/**
	 * @param username
	 * @return
	 */
	public UserResponse searchTweetOfUser(String username);
}
