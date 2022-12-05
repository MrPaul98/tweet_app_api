/**
 * 
 */
package com.tweetapp.tweetappserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweetapp.tweetappserver.models.SessionUserOTP;

/**
 * @author cogjava3341
 *
 */
public interface SessionRepository extends JpaRepository<SessionUserOTP, Integer>{
	
	public SessionUserOTP findByUserName(String username);

}
