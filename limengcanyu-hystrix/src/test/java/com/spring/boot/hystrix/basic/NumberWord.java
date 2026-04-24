package com.spring.boot.hystrix.basic;

public class NumberWord {
    private final Integer number;
    private final String word;

    public NumberWord(final Integer number, final String word) {
        super();
        this.number = number;
        this.word = word;
    }

    public Integer getNumber() {
        return number;
    }

    public String getWord() {
        return word;
    }
}
