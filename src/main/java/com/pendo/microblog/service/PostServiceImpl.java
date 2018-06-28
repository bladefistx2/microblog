package com.pendo.microblog.service;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pendo.microblog.model.Post;
import com.pendo.microblog.repo.PostRepository;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
    private PostRepository postRepository;
    
    @PostConstruct
	public void init() {
    	
    
	 }

	
    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public Optional<Post> findOne(String id) {
    	return postRepository.findById(id);
    }

    public Iterable<Post> findAll() {
        return postRepository.findAll();
    }

}