/**
 * 
 */
package com.tweetapp.tweetappserver.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweetapp.tweetappserver.models.Users;

/**
 * @author cogjava3341
 *
 */
@Repository
public interface UserRepository extends JpaRepository<Users, Integer>{

	public Users findByEmail(String email);
	
	public Users findByLoginId(String username);
	
	public Users findByLoginIdOrEmail(String login,String email);
	
	@Query(value = "select * from users where login_id = ?1" , nativeQuery = true)
	public Users searchUser(String login);
	
	@Query(value = "select * from users where (email = ?1 OR login_id = ?2) and password = ?3" , nativeQuery = true)
	public Users findByEmailOrLoginIdAndPassword(String email, String login, String password);
	
	@Transactional
	@Modifying
	@Query(value = "update users set password = ?1 where email = ?2 OR login_id = ?3" , nativeQuery = true)
	public void updatePassword(String password, String email, String login);
}
