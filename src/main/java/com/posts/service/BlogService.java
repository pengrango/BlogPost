package com.posts.service;

import com.posts.entities.BlogEntity;
import com.posts.exception.PostNotFoundException;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    public List<BlogEntity> getAllBlogs();
    public void addNewPost(BlogEntity blog);
    public void updatePost(BlogEntity blog) throws PostNotFoundException;
    public void deletePost(String id) throws PostNotFoundException;
    public Optional<BlogEntity> findPost(String id);
}
