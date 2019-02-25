package com.posts.dao;

import com.posts.entities.BlogEntity;
import com.posts.exception.PostNotFoundException;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<BlogEntity> get(String id) throws PostNotFoundException;

    List<T> getAll();

    void save(T t);

    void update(T t) throws PostNotFoundException;

    void delete(String id) throws PostNotFoundException;
}
