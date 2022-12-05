package com.tweetapp.tweetappserver.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "sessionuser")
public class SessionUserOTP {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer sessionId;
	private String userName;
	private String oneTimePassword;
	
	
}
