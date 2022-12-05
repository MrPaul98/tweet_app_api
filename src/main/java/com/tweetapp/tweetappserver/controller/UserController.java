package com.tweetapp.tweetappserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.tweetappserver.models.ChangePassword;
import com.tweetapp.tweetappserver.models.LoginUser;
import com.tweetapp.tweetappserver.models.Users;
import com.tweetapp.tweetappserver.response.UserResponse;
import com.tweetapp.tweetappserver.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/v1.0/tweets")
public class UserController {

	@Autowired
	private UserService service;

	/**
	 * @param users
	 * @return
	 */
	@PostMapping("/register")
	public UserResponse createUser(@RequestBody Users users) {

		return this.service.createUser(users);
	}
	
	/**
	 * @param users
	 * @return
	 */
	@PostMapping("/login")
	public UserResponse loginUser(@RequestBody LoginUser users) {

		return this.service.loginUser(users);
	}
	
	@GetMapping("/logout/{user}")
	public UserResponse logoutUser(@PathVariable("user") String user) {
		
		return this.service.logoutUser(user);
	}
	
	/**
	 * @param username
	 * @return
	 */
	@GetMapping("/{username}/forgot")
	public UserResponse forgotPassword(@PathVariable("username") String username) {
		UserResponse response = new UserResponse();
		
		if(this.service.sendMail(username)) {
			response.setMessage("OTP has been send you email Id " + username);
			response.setStatus(200);
		}else {
			response.setMessage("Please provide a valid email Id " + username);
			response.setStatus(400);
		}
		return response;
	}
	
	/**
	 * @param username
	 * @return
	 */
	@PostMapping("/changePassword")
	public UserResponse changePassword(@RequestBody ChangePassword changePassword) {
		
		 return this.service.changePassword(changePassword);
	}
	/**
	 * @return
	 */
	@GetMapping("/users/all")
	public UserResponse getAllUser() {

		return this.service.allUsers();
	}
	
	/**
	 * @param username
	 * @return
	 */
	@GetMapping("/user/search/{username}")
	public UserResponse getUserBySearch(@PathVariable("username") String username) {

		return this.service.searchUserWithUserName(username);
	}
	

}
