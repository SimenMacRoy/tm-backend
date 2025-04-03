package com.example.tmbackend.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
