/**
 * 
 */
package com.tweetapp.tweetappserver.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.tweetappserver.models.TweetLikeTracker;

/**
 * @author cogjava3341
 *
 */
@Repository
public interface LikeTrackerRepository extends JpaRepository<TweetLikeTracker, Integer>{
	
	public TweetLikeTracker findByUsernameAndTweetId(String username, Integer tweetId);
	
	@Transactional
	public void deleteAllByTweetId(Integer Id);

}
