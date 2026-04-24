package com.spring.boot.tomcat.exceptions;

public class PermissionException extends RuntimeException{
    private String message;

    public PermissionException(String message) {
        super(message);
        this.message = message;
    }
}
