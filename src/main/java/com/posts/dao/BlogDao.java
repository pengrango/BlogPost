package com.posts.dao;

import com.posts.entities.BlogEntity;
import com.posts.exception.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BlogDao implements Dao<BlogEntity> {

    Map<String, BlogEntity> blogStore;

    @Autowired
    public BlogDao(Map<String, BlogEntity> blogStore) {
        this.blogStore = blogStore;
    }

    @Override
    public Optional<BlogEntity> get(String id){
        if (!blogStore.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(blogStore.get(id));
    }

    @Override
    public List<BlogEntity> getAll() {
        ArrayList<BlogEntity> blogs = new ArrayList<>(blogStore.values());
        return blogs;
    }

    @Override
    public void save(BlogEntity blog) {
        this.blogStore.put(blog.getId(), blog);
    }

    @Override
    public void update(BlogEntity blog) throws PostNotFoundException {
        if (!blogStore.containsKey(blog.getId())) {
            throw new PostNotFoundException("Post id not found: " + blog.getId());
        }

        blogStore.put(blog.getId(), blog);
    }

    @Override
    public void delete(String id) throws PostNotFoundException {
        if (!blogStore.containsKey(id)) {
            throw new PostNotFoundException("Post id not found: " + id);
        }
        blogStore.remove(id);
    }
}
