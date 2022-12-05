package com.tweetapp.tweetappserver.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;
	private String firstName;
	private String lastName;
	private String gender;
	private String loginId;
	private String contactNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/YYYY")
	private Date dob;
	private String email;
	private String password;

}
