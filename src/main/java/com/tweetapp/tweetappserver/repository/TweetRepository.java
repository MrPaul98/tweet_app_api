/**
 * 
 */
package com.tweetapp.tweetappserver.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweetapp.tweetappserver.models.TweetMessage;
import com.tweetapp.tweetappserver.models.TweetMessageReponse;

/**
 * @author cogjava3341
 *
 */
@Repository
public interface TweetRepository extends JpaRepository<TweetMessage, Integer>{

	public List<TweetMessage> findByUsername(String username);
	
	public List<TweetMessage> findByTwtParentId(Integer id);
	
	@Query(value = "select * from tweetmessage where username = ?1" , nativeQuery = true)
	public List<TweetMessage> seachTweet(String username);
	
	@Transactional
	public Integer deleteByTwtId(Integer Id);
	
	@Transactional
	public void deleteAllByTwtParentId(Integer Id);
	
	@Transactional
	@Modifying
	@Query(value = "update tweetmessage set message = ?1 where twt_id = ?2" , nativeQuery = true)
	public Integer updateTweetMessage(String message, Integer twtId);
	
	@Transactional
	@Modifying
	@Query(value = "Update tweetmessage set twt_like = ?1 where twt_id = ?2" , nativeQuery = true)
	public Integer updateTweetLike(Integer like, Integer twtId);
	
	
}
