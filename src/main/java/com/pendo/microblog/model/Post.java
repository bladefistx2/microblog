package com.pendo.microblog.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "microblog", type = "post")
public class Post {
	
	@Id
	String id;
	
	@Field(type = FieldType.Text)
	String text;
	
	@Field(type = FieldType.Integer)
	Integer rating;
	
	@Field(type = FieldType.Nested)
	Map<String, Integer> votes;
	
	@Field(type = FieldType.Long)
	Long creationDate;

	public Post() {
		super();
		this.rating = 0;
		this.id = UUID.randomUUID().toString();
		this.votes = new HashMap<String, Integer>();
		this.creationDate = new Date().getTime();
	}

	public Post(String text) {
		super();
		this.rating = 0;
		this.id = UUID.randomUUID().toString();
		this.text = text;
		this.votes = new HashMap<String, Integer>();
		this.creationDate = new Date().getTime();
	}
	
	public Post(String id, String text, Integer rating, Map<String, Integer> votes) {
		super();
		this.rating = 0;
		this.id = id;
		this.text = text;
		this.rating = rating;
		this.votes = votes;
		this.creationDate = new Date().getTime();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Map<String, Integer> getVotes() {
		return votes;
	}

	public void setVotes(Map<String, Integer> votes) {
		this.votes = votes;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

}
