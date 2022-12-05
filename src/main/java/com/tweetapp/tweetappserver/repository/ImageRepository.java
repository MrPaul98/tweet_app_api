/**
 * 
 */
package com.tweetapp.tweetappserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.tweetappserver.models.ImageModel;

/**
 * @author cogjava3341
 *
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageModel, String>{

	public Optional<ImageModel> findByEmail(String email);
}
