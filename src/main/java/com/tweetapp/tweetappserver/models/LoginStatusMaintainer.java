package com.tweetapp.tweetappserver.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loginStatus")
public class LoginStatusMaintainer {
	
	@Id
	private Integer userId;
	
	private String email;
	
	private String status;

}
