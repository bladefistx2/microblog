package com.pendo.microblog;

import static org.junit.Assert.assertNotNull;

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
    public void testSave() {
    	
    	Post s = new Post("bla bla bla...");
    	Post test = postService.save(s);

        assertNotNull(s.getId());
        assertNotNull(test.getId());

    }

	@Test
	public void contextLoads() {
	}

}
