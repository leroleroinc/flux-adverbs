package com.lerolero.adverbs.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("adverbs")
public class Adverb {

	@Id
	private String id;
	private String string;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

}
