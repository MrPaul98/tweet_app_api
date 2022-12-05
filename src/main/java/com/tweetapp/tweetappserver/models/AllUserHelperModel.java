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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AllUserHelperModel {

	private String username;
	private String name;
	private String phoneNumber;
	private byte[] picByte;
}
