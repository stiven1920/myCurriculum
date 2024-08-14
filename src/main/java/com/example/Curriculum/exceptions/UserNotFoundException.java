package com.example.Curriculum.exceptions;

public class UserNotFoundException extends RuntimeException {

    // Constructor que acepta un mensaje
    public UserNotFoundException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
