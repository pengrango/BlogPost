package com.posts.controller;

import com.posts.entities.BlogEntity;
import com.posts.exception.PostNotFoundException;
import com.posts.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("blog-web")
public class BlogController {

    @Autowired
    BlogService service;

    @RequestMapping(value = "/posts", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<BlogEntity> postNew(@Valid @RequestBody BlogEntity blog, UriComponentsBuilder b) {
        service.addNewPost(blog);
        UriComponents uriComponents =
                b.path("/blog-web/posts/{id}").buildAndExpand(blog.getId());
        return ResponseEntity.created(uriComponents.toUri())
                .body(blog);
    }

    @RequestMapping("/posts")
    public List<BlogEntity> getAllPosts() {
        List list = service.getAllBlogs();
        return list;
    }

    @RequestMapping(value = "/posts", method = RequestMethod.PUT)
    public ResponseEntity<BlogEntity> updatePost(@Valid @RequestBody BlogEntity blog, HttpServletResponse response) throws PostNotFoundException {
        service.updatePost(blog);
        return new ResponseEntity<>(blog, HttpStatus.CREATED);
    }


    @RequestMapping("/posts/{postId}")
    public ResponseEntity<BlogEntity> getPost(@PathVariable("postId") String postId) {
        Optional<BlogEntity> post = service.findPost(postId);
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @RequestMapping(value = "/posts/{postId}",  method = RequestMethod.DELETE)
    public String deletePost(@PathVariable("postId") String postId) throws PostNotFoundException {
        service.deletePost(postId);
        return "successful operation";
    }

}
