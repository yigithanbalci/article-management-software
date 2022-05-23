package com.example.articlemanagement.exception;

public class BadRequestException extends ArticleManagementException {

    private static final String MESSAGE = "Bad request";

    public BadRequestException() {
        super(MESSAGE);
    }

    public BadRequestException(String message) {
        super(MESSAGE + ": " + message);
    }
}