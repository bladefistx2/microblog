package com.pendo.microblog.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pendo.microblog.model.Post;
import com.pendo.microblog.service.PostService;

@Controller
public class PostController {
	
	private PostService postService;
	
	@Autowired
    public void setPostService(PostService postService){
        this.postService = postService;
    }
	
	@PostMapping("/post/")
	@ResponseBody
	public Object createPost(@RequestParam String text) {
		Post post = new Post(text);
		return postService.save(post);
	}
	
	@GetMapping("/post/{id}")
	@ResponseBody
	public Object getPost(@PathVariable String id) {
		return postService.findOne(id);
	}
	
	@PatchMapping("/post/{id}")
	@ResponseBody
	public Object updatePost(@RequestParam String text, @PathVariable String id) {
		Optional<Post> post = postService.findOne(id);
		if (!post.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Post postToUpdate = post.get();
		postToUpdate.setText(text);
		return postService.save(postToUpdate);
	}
	
	@DeleteMapping("/post/{id}")
	@ResponseBody
	public Object deletePost(@PathVariable String id) {
		postService.delete(postService.findOne(id).get());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/post/{id}/upvote")
	@ResponseBody
	public Object upvotePost(@PathVariable String id, @RequestParam String userId) {
		Optional<Post> post = postService.findOne(id);
		if (!post.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Post postToUpvote = post.get();
		post = postService.vote(postToUpvote, userId, true);
		if (post.isPresent()) {
			return post.get();
		}
		return ResponseEntity.badRequest().body("Cannot upvote more than once.");
	}
	
	@PostMapping("/post/{id}/downvote")
	@ResponseBody
	public Object downvotePost(@PathVariable String id, @RequestParam String userId) {
		Optional<Post> post = postService.findOne(id);
		if (!post.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Post postToDownvote = post.get();
		post = postService.vote(postToDownvote, userId, false);
		if (post.isPresent()) {
			return post.get();
		}
		return ResponseEntity.badRequest().body("Cannot downvote more than once.");
	}
	
	@GetMapping("/post/top_posts")
	@ResponseBody
	public Object getTopPosts() {
		// TODO: implement
		return postService.findAll();
	}
	
}
