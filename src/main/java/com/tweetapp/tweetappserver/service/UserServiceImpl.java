package com.tweetapp.tweetappserver.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tweetapp.tweetappserver.exception.UserException;
import com.tweetapp.tweetappserver.models.AllUserHelperModel;
import com.tweetapp.tweetappserver.models.ChangePassword;
import com.tweetapp.tweetappserver.models.ImageModel;
import com.tweetapp.tweetappserver.models.LoginStatusMaintainer;
import com.tweetapp.tweetappserver.models.LoginUser;
import com.tweetapp.tweetappserver.models.SessionUserOTP;
import com.tweetapp.tweetappserver.models.Users;
import com.tweetapp.tweetappserver.repository.ImageRepository;
import com.tweetapp.tweetappserver.repository.LoginStatusRepository;
import com.tweetapp.tweetappserver.repository.SessionRepository;
import com.tweetapp.tweetappserver.repository.UserRepository;
import com.tweetapp.tweetappserver.response.UserResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private LoginStatusRepository loginStatusRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Value("${spring.mail.username}") 
	private String sender;
	
    @Autowired 
    private JavaMailSender javaMailSender;
     
	
	
	/**
	 *
	 */
	@Override
	public UserResponse createUser(Users users) {
		log.info("Inside create User method");
		UserResponse response = new UserResponse();
		  try {
				Users getUser = this.userRepository.findByEmail(users.getEmail());
				if(getUser != null) {
					throw new UserException("User already exist. Please try with new email Id.");
				}else {
					getUser = this.userRepository.save(users);
					response.setResponseObject(getUser);
					response.setStatus(200);
					response.setMessage("User was created sucessfully");
					LoginStatusMaintainer statusMaintainer = new LoginStatusMaintainer();
					statusMaintainer.setEmail(getUser.getEmail());
					statusMaintainer.setUserId(getUser.getUserId());
					statusMaintainer.setStatus("N");
					this.loginStatusRepository.save(statusMaintainer);
					log.info("User was created sucessfully : {} ", users.getEmail());
				}
			  
		  }catch (UserException exception) {
			  	log.error("User already exists : {} ", users.getEmail());
				response.setResponseObject(users);
				response.setStatus(400);
				response.setMessage(exception.getExceptionMessage());
		  }
	  log.info("exit create User method");
	  return response;
	}

	@Override
	public UserResponse loginUser(LoginUser loginUser) {
		log.info("Inside login User method");
		UserResponse response = new UserResponse();
		  try {
				Users getUser = this.userRepository.findByEmailOrLoginIdAndPassword(loginUser.getUserId(), loginUser.getUserId(), loginUser.getPassword());
				if(getUser != null) {
					response.setResponseObject(new 	Users(getUser.getUserId(), getUser.getFirstName(), getUser.getLastName(), getUser.getGender(),
							getUser.getLoginId(), getUser.getContactNumber(), getUser.getDob(), getUser.getEmail(), null));
					response.setStatus(200);
					response.setMessage("User was login sucessfully");
					Integer rowAffected = this.loginStatusRepository.updateLoginStatus(getUser.getEmail(), "Y");
					log.info("User login sucessfully : {} ", loginUser.getUserId());
				}else {
					throw new UserException("User not found invalid credentials, Please provide correct user details.");
				}
			  
		  }catch (UserException exception) {
			  	log.error("User not found invalid credentials : {} ", loginUser.getUserId());
				response.setResponseObject(loginUser);
				response.setStatus(401);
				response.setMessage(exception.getExceptionMessage());
		  }
	  log.info("exit login User method");
	  return response;
	}

	@Override
	public UserResponse allUsers() {
		log.info("Inside allUsers method");
		UserResponse response = new UserResponse();
		List<AllUserHelperModel> helperModels = new ArrayList<AllUserHelperModel>();
		  try {
				List<Users> getUser = this.userRepository.findAll();
				if(getUser != null) {
					getUser.forEach(userObj ->{
						AllUserHelperModel helperModel = new AllUserHelperModel();
						final Optional<ImageModel> retrievedImage = this.imageRepository.findByEmail(userObj.getEmail());
						helperModel.setName(userObj.getFirstName() + " " + userObj.getLastName());
						helperModel.setUsername(userObj.getLoginId());
						helperModel.setPhoneNumber(userObj.getContactNumber());
						helperModel.setPicByte(decompressBytes(retrievedImage.get().getPicByte()));
						helperModels.add(helperModel);
					});
					response.setResponseObject(helperModels);
					response.setStatus(200);
					response.setMessage("All User found sucessfully");
					log.info("All User found sucessfully");
				}else {
					throw new UserException("No user found.");
				}
			  
		  }catch (UserException exception) {
			  	log.error("No user found.");
				response.setResponseObject(null);
				response.setStatus(400);
				response.setMessage(exception.getExceptionMessage());
		  }
	  log.info("exit allUsers method");
	  return response;
	}

	@Override
	public UserResponse searchUserWithUserName(String username) {
		log.info("Inside searchUserWithUserName method");
		UserResponse response = new UserResponse();
		  try {
			  Users getUser = this.userRepository.findByLoginId(username);
				if(getUser != null) {
					response.setResponseObject(getUser);
					response.setStatus(200);
					response.setMessage("User found sucessfully");
					log.info("User found sucessfully : {} ", getUser.getLoginId());
				}else {
					throw new UserException("User Not found.");
				}
			  
		  }catch (UserException exception) {
			  	log.error("User Not found.");
				response.setResponseObject(null);
				response.setStatus(400);
				response.setMessage(exception.getExceptionMessage());
		  }
	  log.info("exit searchUserWithUserName method");
	  return response;
	}

	public char[] OTP(int len)
	{
		String numbers = "0123456789";
		Random rndm_method = new Random();
		char[] otp = new char[len];

		for (int i = 0; i < len; i++){
			otp[i] =
			numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}
	
	@Override
	public boolean sendMail(String username) {
		SessionUserOTP sessionUserOTP = new SessionUserOTP();
		char[] otp = this.OTP(4);
		try {
	            // Creating a simple mail message
	            SimpleMailMessage mailMessage
	                = new SimpleMailMessage();
	            // Setting up necessary details
	            mailMessage.setFrom(sender);
	            mailMessage.setTo(username);
	            mailMessage.setText("This is your OTP : " + String.valueOf(otp));
	            mailMessage.setSubject("This is your One Time Password");
	 
	            // Sending the mail
	            javaMailSender.send(mailMessage);
	            sessionUserOTP.setUserName(username);
	            sessionUserOTP.setOneTimePassword(String.valueOf(otp));
	            sessionRepository.save(sessionUserOTP);
	        }catch (MailParseException e) {
	        	log.error("failure Mail Send Exception : {}  " , e);
	        	return false;
			}catch (MailAuthenticationException e) {
				log.error("failure Mail Send Exception : {} " , e);
				return false;
			}catch (MailSendException  e) {
				log.error("failure Mail Send Exception : {} " , e);
				return false;
			}catch (Exception e) {
	        	return false;
	        }
		return true;
	}

	@Override
	public UserResponse changePassword(ChangePassword changePassword) {
		log.info("Inside changePassword method");
		UserResponse response = new UserResponse();
		  try {
				SessionUserOTP session = this.sessionRepository.findByUserName(changePassword.getUsername());
				if(session != null && changePassword.getOtp().equalsIgnoreCase(session.getOneTimePassword())) {
					this.userRepository.updatePassword(changePassword.getPassword(), changePassword.getUsername(), changePassword.getUsername());
					this.sessionRepository.deleteById(session.getSessionId());
					Users getUser = this.userRepository.findByEmail(changePassword.getUsername());
					response.setResponseObject(getUser);
					response.setStatus(200);
					response.setMessage("Password was sucessfully updated");
					log.info("Password was sucessfully updated for : {} ", getUser.getLoginId());
				}else {
					throw new UserException("Invalid OTP.");
				}
			  
		  }catch (UserException exception) {
			  	log.error("Invalid OTP.");
				response.setResponseObject(null);
				response.setStatus(400);
				response.setMessage(exception.getExceptionMessage());
		  }
	  log.info("exit changePassword method");
	  return response;
	}

	@Override
	public UserResponse logoutUser(String user) {
		log.info("Inside logoutUser method");
		UserResponse response = new UserResponse();
		  try {
				Integer rowAffected = this.loginStatusRepository.updateLoginStatus(user, "N");
				if(rowAffected != null && rowAffected > 0) {
					response.setStatus(200);
					response.setMessage("User logout sucessfully");
					log.info("User logout sucessfully : {} ", user);
				}else {
					throw new UserException("User Not found.");
				}
			  
		  }catch (UserException exception) {
			  	log.error("User Not found.");
				response.setResponseObject(null);
				response.setStatus(400);
				response.setMessage(exception.getExceptionMessage());
		  }
	  log.info("exit logoutUser method");
	  return response;
	}

	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
}
