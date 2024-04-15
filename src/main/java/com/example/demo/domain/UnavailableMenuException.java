package com.example.demo.domain;

public class UnavailableMenuException extends RuntimeException{
    public UnavailableMenuException(String message) {
        super(message);
    }
}
