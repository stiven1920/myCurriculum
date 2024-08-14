package com.example.Curriculum.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    // Constructor que acepta un mensaje
    public InvalidCredentialsException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
