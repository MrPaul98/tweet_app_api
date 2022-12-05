/**
 * 
 */
package com.tweetapp.tweetappserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.tweetappserver.models.TweetMessage;
import com.tweetapp.tweetappserver.response.UserResponse;
import com.tweetapp.tweetappserver.service.TweetService;

/**
 * @author cogjava3341
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/v1.0/tweets")
public class TweetController {
	
	@Autowired
	private TweetService tweetService;

	@PostMapping("/{username}/add")
	public UserResponse postTweet(@RequestBody TweetMessage message, @PathVariable("username") String username) {
		
		return this.tweetService.postNewTweet(message, username);
	}
	
	@PostMapping("/{username}/reply")
	public UserResponse replyTweet(@RequestBody TweetMessage message, @PathVariable("username") String username) {
		
		return this.tweetService.replyTweet(message, username);
	}
	
	@GetMapping("/all")
	public UserResponse getAllTweet() {
		
		return this.tweetService.getAllTweet();
		
	}
	
	@GetMapping("/reply/all/{id}")
	public UserResponse getReplyAllTweet(@PathVariable("id") Integer id) {
		
		return this.tweetService.getReplyAllTweet(id);
		
	}
	
	@GetMapping("/username/{username}")
	public UserResponse getAllTweetOfUser(@PathVariable("username") String username) {
		
		return this.tweetService.getAllTweetOfUser(username);
		
	}
	
	@GetMapping("/search/{username}")
	public UserResponse searchTweetOfUser(@PathVariable("username") String username) {
		
		return this.tweetService.searchTweetOfUser(username);
		
	}
	
	@PutMapping("/update/{id}")
	public UserResponse updateTweet(@RequestBody TweetMessage message, @PathVariable("id") Integer id) {
		
		return this.tweetService.updateTweet(message, id);
		
	}
	
	@PutMapping("/{username}/like/{id}")
	public UserResponse likeTweet(@PathVariable("username") String username, @PathVariable("id") Integer id) {
		
		return this.tweetService.likeTweet(username, id);
		
	}
	
	@DeleteMapping("/delete/{id}")
	public UserResponse deleteTweet(@PathVariable("id") Integer id) {
		
		return this.tweetService.deleteTweet(id);
		
	}
}
