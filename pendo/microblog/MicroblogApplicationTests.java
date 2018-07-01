package com.pendo.microblog;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.pendo.microblog.model.Post;
import com.pendo.microblog.service.PostService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MicroblogApplication.class)
public class MicroblogApplicationTests {
	
   	@Autowired
    private PostService postService;

    @Autowired
    private ElasticsearchTemplate esTemplate;
    
    @Before
    public void before() {
        esTemplate.deleteIndex(Post.class);
        esTemplate.createIndex(Post.class);
        esTemplate.putMapping(Post.class);
        esTemplate.refresh(Post.class);
    }
    
    @Test
    public void testSavePost() {
    	
    	Post s = new Post("bla bla bla...");
    	Post test = postService.save(s);

        assertNotNull(s.getId());
        assertNotNull(test.getId());

    }
    
    @Test
    public void testUpvotePost() {
    	
    	Post postToUpvote = new Post("The quick brown fox...");
    	postToUpvote = postService.save(postToUpvote);
    	
    	postService.vote(postToUpvote, "1", true);

        assertNotNull(postToUpvote);
        assertTrue(postToUpvote.getRating() == 1);
        
        postService.vote(postToUpvote, "1", true);
        
        assertTrue(postToUpvote.getRating() == 1);
        
        postService.vote(postToUpvote, "2", true);
        
        assertTrue(postToUpvote.getRating() == 2);

    }
    
    @Test
    public void testDownvotePost() {
    	
    	Post postToDownvote = new Post("Red fish blue fish.");
    	postToDownvote = postService.save(postToDownvote);
    	
    	postService.vote(postToDownvote, "1", false);

        assertNotNull(postToDownvote);
        assertTrue(postToDownvote.getRating() == -1);
        
        postService.vote(postToDownvote, "1", false);
        
        assertTrue(postToDownvote.getRating() == -1);
        
        postService.vote(postToDownvote, "2", false);
        
        assertTrue(postToDownvote.getRating() == -2);

    }
    
    @Test
    public void testDownvoteAndUpvotePost() {
    	
    	Post post = new Post("Red fish blue fish.");
    	post = postService.save(post);
    	
    	postService.vote(post, "1", true);

        assertNotNull(post);
        assertTrue(post.getRating() == 1);
        
        postService.vote(post, "1", false);
        
        assertTrue(post.getRating() == 0);
        
        postService.vote(post, "2", true);
        
        assertTrue(post.getRating() == 1);

    }
    

	@Test
	public void contextLoads() {
	}

}
