package com.example.articlemanagement.exception;

public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(String message){
        super(message);
    }
}