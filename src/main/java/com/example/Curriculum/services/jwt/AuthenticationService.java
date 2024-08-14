package com.example.Curriculum.services.jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.Curriculum.dto.AuthenticationRequest;
import com.example.Curriculum.dto.AuthenticationResponse;
import com.example.Curriculum.models.Usuario;
import com.example.Curriculum.services.UsuarioServices;

@Service
public class AuthenticationService {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsuarioServices userServices;

	@Autowired
	private JwtService jwtService;

	public AuthenticationResponse login(AuthenticationRequest authRequest) {

		// Crea el token de autenticación con el nombre de usuario y contraseña
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				authRequest.getUsername(), authRequest.getPassword());

		// Autentica el token usando el AuthenticationManager
		authenticationManager.authenticate(authToken);

		// Recupera el usuario de la base de datos
		Optional<Usuario> optionalUser = userServices.findUserName(authRequest.getUsername());
		Usuario user = optionalUser.get();

		// Genera el token JWT
		String jwt = jwtService.generateToken(user, generateExtraClaims(user));

		// Devuelve una respuesta con el token y el usuario
		return new AuthenticationResponse(jwt, user);

	}

	private Map<String, Object> generateExtraClaims(Usuario user) {

		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("name", user.getNombre());
		extraClaims.put("role", user.getRole().name());
		extraClaims.put("permissions", user.getRole());

		return extraClaims;
	}
}