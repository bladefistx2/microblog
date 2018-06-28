package com.pendo.microblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pendo.microblog.Const;
import com.pendo.microblog.service.PostService;

@Controller
public class PostController {
	
	private PostService postService;
	
	@Autowired
    public void setPostService(PostService postService){
        this.postService = postService;
    }
	
	@GetMapping("/post/{id}")
	@ResponseBody
	public Object getPost(@PathVariable String id) {
		return postService.findOne(id);
	}
	
}
