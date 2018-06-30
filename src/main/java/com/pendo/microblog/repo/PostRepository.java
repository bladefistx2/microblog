package com.pendo.microblog.repo;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.pendo.microblog.model.Post;

public interface PostRepository extends ElasticsearchRepository<Post, String> {

    Optional<Post> findById(String id);
    
}