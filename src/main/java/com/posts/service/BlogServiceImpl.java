package com.posts.service;

import com.posts.dao.BlogDao;
import com.posts.entities.BlogEntity;
import com.posts.exception.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements  BlogService{

    BlogDao blogDao;

    @Autowired
    public BlogServiceImpl(BlogDao blogDao ) {
        this.blogDao = blogDao;
    }

    @Override
    public List<BlogEntity> getAllBlogs() {
        return blogDao.getAll();
    }

    @Override
    public void addNewPost(BlogEntity blog)  {
        blogDao.save(blog);
    }

    @Override
    public void updatePost(BlogEntity blog) throws PostNotFoundException {
        if (!isPostExist(blog.getId())) {
            throw new PostNotFoundException("Post not found");
        }
        blogDao.update(blog);
    }

    @Override
    public void deletePost(String id) throws PostNotFoundException {
        if (!isPostExist(id)) {
            throw new PostNotFoundException("Post not found");
        }
        blogDao.delete(id);
    }

    @Override
    public Optional<BlogEntity> findPost(String id) {
        Optional<BlogEntity> blog = blogDao.get(id);
        return blog;
    }

    private boolean isPostExist(String id) {
        Optional<BlogEntity> blogDomain1 = blogDao.get(id);
        if (blogDomain1.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
