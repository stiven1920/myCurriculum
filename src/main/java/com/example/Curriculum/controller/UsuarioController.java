package com.example.Curriculum.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Curriculum.models.Usuario;
import com.example.Curriculum.services.UsuarioServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UsuarioController {

	@Autowired
	private UsuarioServices userServices;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PreAuthorize("permitAll")
	@PostMapping(path = "/saveUser", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveUser(
			@RequestHeader(value = HttpHeaders.CONTENT_TYPE, defaultValue = "") String contentType,
			@RequestBody @Valid Usuario users) {
		if (!MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
			Map<String, Object> map = new HashMap<>();
			map.put("error", "El encabezado 'Content-Type' debe ser 'application/json'");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		}

		Map<String, Object> map = new HashMap<>();
		try {
			Optional<Usuario> userOptional = userServices.getUserByDni(users.getCedula());
			if (!userOptional.isPresent()) {
				String encodedPassword = passwordEncoder.encode(users.getPassword());
				users.setPassword(encodedPassword);
				Usuario addUser = userServices.saveUser(users);
				map.put("new_user", addUser);
				return ResponseEntity.status(HttpStatus.CREATED).body(map);
			} else {
				map.put("message", "Usuario ya existe con esta Cedula: " + users.getCedula());
				return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
			}
		} catch (Exception e) {
			map.put("message", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("hasAuthority('READ_ADMINISTRATOR') and hasAuthority('READ_CUSTOMER')")
	@GetMapping(path = "/getUserBy")
	public ResponseEntity<Object> getUserBy(@RequestParam BigInteger cedula) {
		try {
			Optional<Usuario> userEncontrado = userServices.getUserByDni(cedula);
			if (userEncontrado.isPresent()) {
				return new ResponseEntity<>(userEncontrado.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>("No se encontró el usuario con cedula: " + cedula, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error al recuperar los usuarios", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("hasAuthority('SAVE_ADMINISTRATOR')and hasAuthority('SAVE_CUSTOMER')")
	@PutMapping(path = "/updateUser")
	public ResponseEntity<Object> updateUser(@RequestParam BigInteger cedula, @RequestBody Usuario user) {
		Map<String, Object> map = new HashMap<>();
		try {
			Optional<Usuario> userEncontrado = userServices.getUserByDni(cedula);
			if (userEncontrado.isPresent()) {
				Usuario userUpdate = userEncontrado.get();
				String encodedPassword = passwordEncoder.encode(user.getPassword());
				userUpdate.setNombre(encodedPassword);
				userUpdate.setApellido(user.getApellido());
				userUpdate.setTelefono(user.getTelefono());
				userUpdate.setUsername(user.getUsername());
				userUpdate.setPassword(encodedPassword);
				userServices.saveUser(userUpdate);
				map.put("Update User", userUpdate);
				return ResponseEntity.status(HttpStatus.OK).body(map);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No se encontró el usuario con cedula: " + cedula);
			}
		} catch (Exception e) {
			map.put("error", "Error al actualizar el usuario");
			map.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}

	@PreAuthorize("hasAuthority('READ_ADMINISTRATOR') or hasAuthority('READ_CUSTOMER')")
	@GetMapping("/getAllUser")
	public ResponseEntity<Object> getAllUsers() {
		try {
			List<Usuario> users = userServices.getAll();
			if (users.isEmpty()) {
				return new ResponseEntity<>("No se encontraron usuarios", HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(users, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error al recuperar los usuarios", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("hasAuthority('READ_ADMINISTRATOR')")
	@DeleteMapping("/deleteUserBy")
	public ResponseEntity<Object> deleteUser(@RequestParam BigInteger cedula) {
		Map<String, Object> map = new HashMap<>();
		try {
			Optional<Usuario> userDelete = userServices.getUserByDni(cedula);
			if (userDelete.isPresent()) {
				boolean deleted = userServices.deleteUser(cedula);
				if (deleted) {
					return ResponseEntity.ok().build();
				} else {
					map.put("error", "Error al eliminar el usuario");
					map.put("message", "No se pudo eliminar el usuario");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
				}
			} else {
				map.put("error", "El usuario con la cédula especificada no existe");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
			}
		} catch (Exception e) {
			map.put("error", "Error al eliminar el usuario");
			map.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}

}
