package com.tweetapp.tweetappserver.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	
	@JsonProperty("responseObject")
	private Object responseObject;
	private String message;
	private Integer status;

}
