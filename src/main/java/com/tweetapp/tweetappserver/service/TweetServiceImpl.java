package com.tweetapp.tweetappserver.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.tweetappserver.exception.UserException;
import com.tweetapp.tweetappserver.models.ImageModel;
import com.tweetapp.tweetappserver.models.TweetLikeTracker;
import com.tweetapp.tweetappserver.models.TweetMessage;
import com.tweetapp.tweetappserver.models.TweetMessageReponse;
import com.tweetapp.tweetappserver.models.Users;
import com.tweetapp.tweetappserver.repository.ImageRepository;
import com.tweetapp.tweetappserver.repository.LikeTrackerRepository;
import com.tweetapp.tweetappserver.repository.TweetRepository;
import com.tweetapp.tweetappserver.repository.UserRepository;
import com.tweetapp.tweetappserver.response.UserResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TweetServiceImpl implements TweetService{
	
	@Autowired
	private TweetRepository tweetRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LikeTrackerRepository likeTrackerRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
//	@KafkaListener(topics = {"message"})
//	public void getTopic(Integer id) {
//		System.out.println(id);
//	}

	@Override
	public UserResponse updateTweet(TweetMessage message, Integer twtId) {
		log.info("Inside update Tweet Message By , {} ", message.getUsername());
		UserResponse response = new UserResponse();
		try {
			Users users =  this.userRepository.findByLoginIdOrEmail(message.getUsername(), message.getUsername());
			if(users != null) {
				log.info("User found now save tweet of user, {} ", users.getFirstName());
				Integer updatedRow = this.tweetRepository.updateTweetMessage(message.getMessage(), twtId);
				response.setMessage("Tweet was updated sucessfully");
				response.setResponseObject(updatedRow);
				response.setStatus(200);
			}else {
				throw new UserException("User Not found.");
			}
		} catch (UserException e) {
			log.error(e.getExceptionMessage());
			response.setMessage(e.getMessage());
			response.setResponseObject(e);
			response.setStatus(400);
		}
		log.info("Exit update Tweet Message By , {} ", message.getUsername());
		return response;
	}

	@Override
	public UserResponse likeTweet(String username, Integer id) {
		log.info("Inside like Tweet Message.");
		UserResponse response = new UserResponse();
		TweetLikeTracker tracker = new TweetLikeTracker();
		try {

			TweetLikeTracker likeTracker = this.likeTrackerRepository.findByUsernameAndTweetId(username, id);
			if(likeTracker == null) {
				Optional<TweetMessage> data = this.tweetRepository.findById(id);
				if(data.isPresent()) {
					Integer like = data.get().getTwtLike() + 1;
					System.out.println(like);
					Integer row = this.tweetRepository.updateTweetLike(like, id);
					if(row > 0) {
						tracker.setUsername(username);
						tracker.setTweetId(id); 
						TweetLikeTracker saveTracker  = this.likeTrackerRepository.save(tracker);
						response.setMessage("like Tweet sucessfully");
						response.setResponseObject(saveTracker);
						response.setStatus(200);
					}else {
						throw new UserException("liked was not updated due to some system error.");
					}
				}
			}else {
				throw new UserException("User already liked this post.");
			}
		} catch (UserException e) {
			log.error(e.getExceptionMessage());
			response.setMessage(e.getExceptionMessage());
			response.setResponseObject(e);
			response.setStatus(400);
		}
		log.info("Exit like Tweet Message.");
		return response;
	}

	@Override
	public UserResponse deleteTweet(Integer id) {
		log.info("Inside delete Tweet Message.");
		UserResponse response = new UserResponse();
		try {

			Integer deleteRow = this.tweetRepository.deleteByTwtId(id);
			this.tweetRepository.deleteAllByTwtParentId(id);
			this.likeTrackerRepository.deleteAllByTweetId(id);
			if(deleteRow != null && deleteRow > 0) {
				response.setMessage("Delete Tweet sucessfully");
				response.setResponseObject(deleteRow);
				response.setStatus(200);
			}else {
				throw new UserException("Tweet Not found.");
			}
		} catch (UserException e) {
			log.error(e.getExceptionMessage());
			response.setMessage(e.getMessage());
			response.setResponseObject(e);
			response.setStatus(400);
		}
		log.info("Exit delete Tweet Message.");
		return response;
	}

	@Override
	public UserResponse replyTweet(TweetMessage message, String username) {
		log.info("Inside Post Reply Tweet Message By , {} ", username);
		UserResponse response = new UserResponse();
		try {
			Users users =  this.userRepository.findByLoginIdOrEmail(username, username);
			if(users != null) {
				log.info("User found now save tweet of user, {} ", users.getFirstName());
				TweetMessage tweetMessage = this.tweetRepository.save(message);
				response.setMessage("Tweet posted sucessfully");
				response.setResponseObject(tweetMessage);
				response.setStatus(200);
			}else {
				throw new UserException("User Not found.");
			}
		} catch (UserException e) {
			log.error(e.getExceptionMessage());
			response.setMessage(e.getMessage());
			response.setResponseObject(e);
			response.setStatus(400);
		}
		log.info("Exit Post Reply Tweet Message By , {} ", username);
		return response;
	}

	@Override
	public UserResponse getAllTweet() {
		log.info("Inside Get All Tweet Message");
		UserResponse response = new UserResponse();
		List<TweetMessageReponse> messageReponses =  new ArrayList<TweetMessageReponse>();
		try {
			List<TweetMessage> messages = new ArrayList<TweetMessage>();
			messages = this.tweetRepository.findAll().stream().filter(obj -> obj.getTwtParentId().equals(0)).collect(Collectors.toList());
			if(messages != null && !messages.isEmpty()) {
				messages.forEach(msgObj ->{
					TweetMessageReponse messageReponse = new TweetMessageReponse();
					final Optional<ImageModel> retrievedImage = this.imageRepository.findByEmail(msgObj.getUsername());
					Users users = this.userRepository.findByEmail(msgObj.getUsername());
					messageReponse.setMessage(msgObj.getMessage());
					messageReponse.setTwtId(msgObj.getTwtId());
					messageReponse.setTwtLike(msgObj.getTwtLike());
					messageReponse.setTwtParentId(msgObj.getTwtParentId());
					messageReponse.setUsername(msgObj.getUsername());
					messageReponse.setPicByte(decompressBytes(retrievedImage.get().getPicByte()));
					messageReponse.setName(users.getFirstName() +" "+ users.getLastName());
					messageReponse.setUserId(users.getLoginId());
					messageReponses.add(messageReponse);
				});
				response.setMessage("Tweet retrived sucessfully");
				response.setResponseObject(messageReponses);
				response.setStatus(200);
			}else {
				throw new UserException("Not Tweet found.");
			}
		} catch (UserException e) {
			log.error(e.getExceptionMessage());
			response.setMessage(e.getMessage());
			response.setResponseObject(e);
			response.setStatus(400);
		}
		log.info("Exit Get All Tweet Message");
		return response;
	}

	@Override
	public UserResponse getAllTweetOfUser(String username) {
		log.info("Inside Get All Tweet Message of user {},", username);
		UserResponse response = new UserResponse();
		try {
			List<TweetMessage> messages = new ArrayList<TweetMessage>();
			messages = this.tweetRepository.findByUsername(username).stream().filter(obj -> obj.getTwtParentId().equals(0)).collect(Collectors.toList());
			if(messages != null && !messages.isEmpty()) {
				response.setMessage("Tweet retrived sucessfully");
				response.setResponseObject(messages);
				response.setStatus(200);
			}else {
				throw new UserException("Not Tweet found.");
			}
		} catch (UserException e) {
			log.error(e.getExceptionMessage());
			response.setMessage(e.getMessage());
			response.setResponseObject(e);
			response.setStatus(400);
		}
		log.info("Exit Get All Tweet Message of user {},", username);
		return response;
	}

	@Override
	public UserResponse postNewTweet(TweetMessage message, String username) {
		log.info("Inside Post Tweet Message By , {} ", username);
		UserResponse response = new UserResponse();
		try {
			Users users =  this.userRepository.findByLoginIdOrEmail(username, username);
			if(users != null) {
				log.info("User found now save tweet of user, {} ", users.getFirstName());
				TweetMessage tweetMessage = this.tweetRepository.save(message);
				response.setMessage("Tweet posted sucessfully");
				response.setResponseObject(tweetMessage);
				response.setStatus(200);
			}else {
				throw new UserException("User Not found.");
			}
		} catch (UserException e) {
			log.error(e.getExceptionMessage());
			response.setMessage(e.getMessage());
			response.setResponseObject(e);
			response.setStatus(400);
		}
		log.info("Exit Post Tweet Message By , {} ", username);
		return response;
	}

	@Override
	public UserResponse getReplyAllTweet(Integer id) {
		log.info("Inside Get Reply All Tweet Message");
		UserResponse response = new UserResponse();
		List<TweetMessageReponse> messageReponses =  new ArrayList<>();
		try {
			List<TweetMessage> messages = this.tweetRepository.findByTwtParentId(id);
			if(messages != null && !messages.isEmpty()) {
				messages.forEach(msgObj ->{
					TweetMessageReponse messageReponse = new TweetMessageReponse();
					final Optional<ImageModel> retrievedImage = this.imageRepository.findByEmail(msgObj.getUsername());
					Users users = this.userRepository.findByEmail(msgObj.getUsername());
					messageReponse.setMessage(msgObj.getMessage());
					messageReponse.setTwtId(msgObj.getTwtId());
					messageReponse.setTwtLike(msgObj.getTwtLike());
					messageReponse.setTwtParentId(msgObj.getTwtParentId());
					messageReponse.setUsername(msgObj.getUsername());
					messageReponse.setPicByte(decompressBytes(retrievedImage.get().getPicByte()));
					messageReponse.setName(users.getFirstName() +" "+ users.getLastName());
					messageReponse.setUserId(users.getLoginId());
					messageReponses.add(messageReponse);
				});
				response.setMessage("Tweet retrived sucessfully");
				response.setResponseObject(messageReponses);
				response.setStatus(200);
			}else {
				throw new UserException("Not Tweet found.");
			}
		} catch (UserException e) {
			log.error(e.getExceptionMessage());
			response.setMessage(e.getMessage());
			response.setResponseObject(e);
			response.setStatus(400);
		}
		log.info("Exit Get Reply All Tweet Message");
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

	@Override
	public UserResponse searchTweetOfUser(String username) {
		log.info("Inside Get search All Tweet Message");
		UserResponse response = new UserResponse();
		List<TweetMessageReponse> messageReponses =  new ArrayList<>();
		try {
			Users user = this.userRepository.searchUser(username);
			if(user != null) {
				List<TweetMessage> messages = this.tweetRepository.seachTweet(user.getEmail());
				if(messages != null && !messages.isEmpty()) {
					messages.forEach(msgObj ->{
						TweetMessageReponse messageReponse = new TweetMessageReponse();
						final Optional<ImageModel> retrievedImage = this.imageRepository.findByEmail(msgObj.getUsername());
						Users users = this.userRepository.findByEmail(msgObj.getUsername());
						messageReponse.setMessage(msgObj.getMessage());
						messageReponse.setTwtId(msgObj.getTwtId());
						messageReponse.setTwtLike(msgObj.getTwtLike());
						messageReponse.setTwtParentId(msgObj.getTwtParentId());
						messageReponse.setUsername(msgObj.getUsername());
						messageReponse.setPicByte(decompressBytes(retrievedImage.get().getPicByte()));
						messageReponse.setName(users.getFirstName() +" "+ users.getLastName());
						messageReponse.setUserId(users.getLoginId());
						messageReponses.add(messageReponse);
					});
					response.setMessage("Tweet retrived sucessfully");
					response.setResponseObject(messageReponses);
					response.setStatus(200);
				}else {
					throw new UserException("Not Tweet found.");
				}
			}else {
				throw new UserException("User Not found.");
			}
		} catch (UserException e) {
			log.error(e.getExceptionMessage());
			response.setMessage(e.getExceptionMessage());
			response.setResponseObject(e);
			response.setStatus(400);
		}
		log.info("Exit Get search All Tweet Message");
		return response;
	}

}
