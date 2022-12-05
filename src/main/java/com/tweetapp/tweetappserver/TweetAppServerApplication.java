package com.tweetapp.tweetappserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableKafka
@SpringBootApplication
public class TweetAppServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetAppServerApplication.class, args);
	}

}
