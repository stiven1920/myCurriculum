package com.example.Curriculum.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Curriculum.dto.AuthenticationRequest;
import com.example.Curriculum.dto.AuthenticationResponse;
import com.example.Curriculum.exceptions.AuthenticationException;
import com.example.Curriculum.exceptions.InvalidCredentialsException;
import com.example.Curriculum.exceptions.UserNotFoundException;
import com.example.Curriculum.models.Usuario;
import com.example.Curriculum.services.UsuarioServices;
import com.example.Curriculum.services.jwt.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UsuarioServices userServices;

    /**
     * Método para autenticar a un usuario y devolver un token JWT si la
     * autenticación es exitosa.
     *
     * @param authRequest La solicitud de autenticación que contiene el nombre de
     *                    usuario y la contraseña.
     * @return Un ResponseEntity que contiene un Map con el token JWT y el estado
     *         HTTP o un mensaje de error.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid AuthenticationRequest authRequest) {
        Map<String, Object> response = new HashMap<>();
    
        try {
            AuthenticationResponse jwtResponse = authenticationService.login(authRequest);
            Optional<Usuario> userAuth = userServices.findUserName(authRequest.getUsername());
    
            // Verifica si el usuario está presente y si coincide con el usuario en jwtResponse
            if (userAuth.isPresent() && jwtResponse.getUser() != null 
                    && jwtResponse.getUser().getUsername().equals(userAuth.get().getUsername())) {
                Usuario user = jwtResponse.getUser();
                Map<String, Object> userMap = new HashMap<>();
    
                response.put("token", jwtResponse.getJwt());
                response.put("usuario", userMap);
    
                userMap.put("nombre", user.getNombre());
                userMap.put("apellido", user.getApellido());
                userMap.put("username", user.getUsername());
                userMap.put("accountNonExpired", user.isAccountNonExpired());
                userMap.put("credentialsNonExpired", user.isCredentialsNonExpired());
                userMap.put("enabled", user.isEnabled());
                response.put("status", HttpStatus.OK.value());
    
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Usuario o contraseña inválidos");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
    
        } catch (InvalidCredentialsException e) {
            response.put("message", "Credenciales inválidas");
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    
        } catch (UserNotFoundException e) {
            response.put("message", "Usuario no encontrado");
            response.put("status", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    
        } catch (Exception e) {
            response.put("message", "Ha ocurrido un problema con el servicio");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    

    /**
     * Maneja las excepciones de autenticación.
     *
     * @param e La excepción de autenticación a manejar.
     * @return Un ResponseEntity que contiene un Map con un mensaje de error.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Fallo en la autenticación: " + e.getMessage());
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
