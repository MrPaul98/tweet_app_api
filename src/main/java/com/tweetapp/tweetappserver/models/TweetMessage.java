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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tweetmessage")
public class TweetMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer twtId;
	private Integer twtParentId;
	private String username;
	private Integer twtLike;
	private String message;
	
}
