package com.example.Curriculum;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.Curriculum.dto.AuthenticationRequest;
import com.example.Curriculum.dto.AuthenticationResponse;
import com.example.Curriculum.services.jwt.AuthenticationService;

public class UsuarioTest {
	
	private AuthenticationService loginServices;
    private AuthenticationRequest loginRequest;

    @BeforeEach
    public void setUp() {
        loginServices = new AuthenticationService(); 
        loginRequest = new AuthenticationRequest(); 
    }
    
    @Test
    public void testLoginReturnsJwt() {
        loginRequest.setUsername("stiven1920");
        loginRequest.setPassword("123456");

        AuthenticationResponse response = loginServices.login(loginRequest);

        assertNotNull(response, "La respuesta de autenticación no debería ser nula");

        String jwt = response.getJwt();

        assertNotNull(jwt, "El JWT no debería ser nulo");
        assertFalse(jwt.isEmpty(), "El JWT no debería estar vacío");

        assertTrue(jwt.startsWith("eyJ"), "El JWT debería comenzar con 'eyJ', típico de un token JWT");
    }


}
