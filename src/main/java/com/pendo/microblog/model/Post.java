package com.pendo.microblog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "microblog", type = "post")
public class Post {
	
	@Id
	String id;
	
	public Post() {
		id = null;
	}

	public Post(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
