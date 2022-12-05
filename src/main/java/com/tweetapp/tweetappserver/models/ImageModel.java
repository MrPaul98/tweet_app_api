package com.tweetapp.tweetappserver.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image_table")
public class ImageModel {

		@Id
		private String email;
		@Column(name = "name")
		private String name;
		@Column(name = "type")
		private String type;
	    //image bytes can have large lengths so we specify a value
	    //which is more than the default length for picByte column
		@Column(name = "picByte", length = 500000000)
		private byte[] picByte;
}
