/**
 * 
 */
package com.tweetapp.tweetappserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cogjava3341
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class UserException extends Exception{

	String exceptionMessage;
}
