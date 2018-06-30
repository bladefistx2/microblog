package com.pendo.microblog.service;

import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pendo.microblog.Const;
import com.pendo.microblog.model.Post;
import com.pendo.microblog.repo.PostRepository;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
    private PostRepository postRepository;
    
    @PostConstruct
	public void init() {
    	Optional<Post> initialPost = postRepository.findById(Const.DEFAULT_POST_ID);
    	if (!initialPost.isPresent()) {

    		Post post = new Post("Default Post");
			post.setId(Const.DEFAULT_POST_ID);
			Post defaultPost = save(post);
			
			assertNotNull(post.getId());
			assertNotNull(defaultPost.getId());
    	}
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
    
	public Optional<Post> vote(Post post, String userId, boolean upvote) {
		int vote;
		if (upvote) {
			vote = 1;
		} else {
			vote = -1;
		}
		Map<String, Integer> votes = post.getVotes();
		Integer userRating = votes.get(userId);
		if (userRating == null || userRating == 0) {
			votes.put(userId, vote);
			post.setRating(post.getRating() + vote);
		} else {
			// User already voted.
			vote += userRating;
			if (vote == 0) {
				// Zero out voting.
				votes.put(userId, vote);
				post.setRating(post.getRating() - userRating);
			} else {
				// Cannot vote twice.
				return Optional.empty();
			}
		}
		return Optional.of(save(post));
	}
}