package com.example.Curriculum.exceptions;

public class AuthenticationException extends RuntimeException {

    // Constructor que acepta un mensaje
    public AuthenticationException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
