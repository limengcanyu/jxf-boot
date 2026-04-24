package com.spring.boot.tomcat.entities;

import lombok.Data;

@Data
public class MyErrorBody {
    private int value;
    private String detailMessage;

    public MyErrorBody() {
    }

    public MyErrorBody(int value, String message) {
        this.value = value;
        this.detailMessage = message;
    }
}
