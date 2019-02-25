package com.posts.exception;

public class PostNotFoundException extends Exception{
    public PostNotFoundException() {
        super();
    }

    public PostNotFoundException(String msg) {
        super(msg);
    }
}
