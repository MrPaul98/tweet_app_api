/**
 * 
 */
package com.tweetapp.tweetappserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cogjava3341
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TweetMessageReponse {

	private Integer twtId;
	private Integer twtParentId;
	private String username;
	private Integer twtLike;
	private String message;
	private byte[] picByte;
	private String name;
	private String userId;
	
}
