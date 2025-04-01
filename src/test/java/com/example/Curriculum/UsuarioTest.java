package com.example.Curriculum;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.Curriculum.controller.AuthenticationController;
import com.example.Curriculum.dto.AuthenticationRequest;
import com.example.Curriculum.dto.AuthenticationResponse;
import com.example.Curriculum.models.Usuario;
import com.example.Curriculum.services.UsuarioServices;
import com.example.Curriculum.services.jwt.AuthenticationService;
import com.example.Curriculum.utils.Role;

@WebMvcTest(controllers = AuthenticationController.class)
public class UsuarioTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private AuthenticationService authenticationService;

	@Mock
	private UsuarioServices userServices;

	@InjectMocks
	private AuthenticationController authenticationController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
	}

	@Test
	public void testLogin_Success() throws Exception {
		AuthenticationRequest authRequest = new AuthenticationRequest();
		authRequest.setUsername("stiven1920");
		authRequest.setPassword("123456");

		AuthenticationResponse jwtResponse = new AuthenticationResponse("jwtToken");

		Usuario user = new Usuario();
		user.setNombre("jhersson stiven");
		user.setApellido("jojoa lozano");
		user.setUsername("stiven1920");
		user.setRole(Role.ADMINISTRATOR);
		user.isAccountNonExpired();
		user.isCredentialsNonExpired();
		user.isEnabled();

		when(authenticationService.login(authRequest)).thenReturn(jwtResponse);
		when(userServices.findUserName("stiven1920")).thenReturn(Optional.of(user));

		Map<String, Object> userMap = new HashMap<>();
		userMap.put("nombre", "jhersson stiven");
		userMap.put("apellido", "jojoa lozano");
		userMap.put("username", "stiven1920");
		userMap.put("role", "ADMINISTRATOR");
		userMap.put("accountNonExpired", true);
		userMap.put("credentialsNonExpired", true);
		userMap.put("enabled", true);

		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\": \"stiven1920\", \"password\": \"123456\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("jwtToken"))
				.andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("$.usuario.nombre").value("jhersson stiven"))
				.andExpect(jsonPath("$.usuario.apellido").value("jojoa lozano"))
				.andExpect(jsonPath("$.usuario.username").value("stiven1920"))
				.andExpect(jsonPath("$.usuario.role").value("ADMINISTRATOR"))
				.andExpect(jsonPath("$.usuario.accountNonExpired").value(true))
				.andExpect(jsonPath("$.usuario.credentialsNonExpired").value(true))
				.andExpect(jsonPath("$.usuario.enabled").value(true));
	}
}
