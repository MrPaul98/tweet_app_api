/**
 * 
 */
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

/**
 * @author cogjava3341
 *
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "liketracker")
public class TweetLikeTracker {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer likeId;
	private Integer tweetId;
	private String username;
}
