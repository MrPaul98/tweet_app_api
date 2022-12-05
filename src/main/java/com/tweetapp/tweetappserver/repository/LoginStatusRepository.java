/**
 * 
 */
package com.tweetapp.tweetappserver.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweetapp.tweetappserver.models.LoginStatusMaintainer;

/**
 * @author cogjava3341
 *
 */
@Repository
public interface LoginStatusRepository extends JpaRepository<LoginStatusMaintainer, Integer>{

	@Transactional
	@Modifying
	@Query(value = "update login_status set status = ?2 where email = ?1" , nativeQuery = true)
	public Integer updateLoginStatus(String email, String status);
}
