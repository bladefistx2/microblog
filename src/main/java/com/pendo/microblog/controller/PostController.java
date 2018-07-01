package com.pendo.microblog.controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pendo.EsClient;
import com.pendo.microblog.Const;
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
		ArrayList<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		
		try {
			Client client = EsClient.getClient();
			
			SearchRequestBuilder elastic = client.prepareSearch()
					.setIndices(Const.INDEX_NAME)
					.setTypes(Const.POST_DOCUMENT_NAME);
			
			MatchAllQueryBuilder matchQuery = QueryBuilders.matchAllQuery();
			
			elastic.setQuery(matchQuery);
			
			elastic.addSort(Const.RATING_FIELD, SortOrder.DESC);
			
			elastic.addSort(Const.CREATION_DATE_FIELD, SortOrder.ASC);
			
			//waitForClusterHealthYellow()
			
			// Execute the search
			final SearchResponse searchResponse = elastic.execute().actionGet();
			SearchHits searchHits = searchResponse.getHits();
			Long totalHits = searchHits.getTotalHits();
			
			SearchHit currentHit;
			
			Iterator<SearchHit> hitsIt = searchHits.iterator();
			while (hitsIt.hasNext()){
				currentHit = hitsIt.next();
				Map<String, Object> sourceAsMap = currentHit.getSourceAsMap();
				if (sourceAsMap == null){
					totalHits--;
					continue;
				}
				sourceAsMap = modifyResults(sourceAsMap);
				
				// Add the result to the list
				results.add(sourceAsMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Search is currently down. Please try again later, or contact support.");
		}
		
		return results;
	}

	private Map<String, Object> modifyResults(Map<String, Object> sourceAsMap) {
		Map<String, Object> res = sourceAsMap;
		Long dateMili = (Long) res.remove(Const.CREATION_DATE_FIELD);
		res.put(Const.CREATION_DATE_FIELD, new Date(dateMili));
		return res;
	}
	
}
