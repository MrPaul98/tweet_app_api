package com.tweetapp.tweetappserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {

	private String username;
	private String otp;
	private String password;
}
