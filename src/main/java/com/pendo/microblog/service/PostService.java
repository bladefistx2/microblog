package com.pendo.microblog.service;

import java.util.Optional;

import com.pendo.microblog.model.Post;

public interface PostService {

	Post save(Post post);

    void delete(Post post);

    Optional<Post> findOne(String id);

    Iterable<Post> findAll();

}