package com.example.Curriculum.controller;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Curriculum.dto.MensajeDto;
import com.example.Curriculum.models.Mensaje;
import com.example.Curriculum.models.Usuario;
import com.example.Curriculum.services.MensajeServices;
import com.example.Curriculum.services.UsuarioServices;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/message")
@CrossOrigin(origins = "*")
public class MensajeController {

	@Autowired
	private MensajeServices msjServices;

	@Autowired
	private UsuarioServices userServices;

	// @PreAuthorize("hasAuthority('SAVE_ADMINISTRATOR')and
	// hasAuthority('SAVE_CUSTOMER')")
	@PostMapping(path = "/saveMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveMensaje(
			@RequestHeader(value = HttpHeaders.CONTENT_TYPE, defaultValue = "") String contentType,
			@RequestBody Mensaje msj) {
		if (!MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "El encabezado 'Content-Type' debe ser 'application/json'");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		try {
			if (msj.getUsuario() != null && msj.getUsuario().getId() != null) {
				Optional<Usuario> usuario = userServices.findById(msj.getUsuario().getId());
				if (usuario.isPresent()) {
					msj.setFecha(LocalDateTime.now());
					Mensaje savedMensaje = msjServices.saveMensaje(msj);
					return new ResponseEntity<>(savedMensaje, HttpStatus.CREATED);
				} else {
					Map<String, Object> errorResponse = new HashMap<>();
					errorResponse.put("error", "Usuario no encontrado");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
				}
			} else {
				Map<String, Object> errorResponse = new HashMap<>();
				errorResponse.put("error", "Usuario o ID del usuario no proporcionados");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
			}
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping("getAllMessage")
	public ResponseEntity<Object> getAllUser() {
		try {
			ArrayList<Object[]> mensaje = msjServices.findMensajeAll();
			if (mensaje != null && !mensaje.isEmpty()) {
				List<MensajeDto> msjList = new ArrayList<>();
				DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

				for (Object[] row : mensaje) {
					MensajeDto mensajeDto = new MensajeDto();
					mensajeDto.setUsername((String) row[0]);
					LocalDateTime fecha = (LocalDateTime) row[1];
					mensajeDto.setFecha(fecha.format(fechaFormatter));
					mensajeDto.setHora(fecha.format(horaFormatter));
					mensajeDto.setMensaje((String) row[2]);
					msjList.add(mensajeDto);
				}

				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writeValueAsString(msjList);
				return ResponseEntity.ok(json);
			}
			return new ResponseEntity<>("No existe el usuario", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al recuperar el  mensaje", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("getMessageByDni")
	public ResponseEntity<Object> getAllUser(@RequestParam BigInteger cedula) {
		try {
			ArrayList<Object[]> mensaje = msjServices.findMensajeByCedula(cedula);
			if (mensaje != null && !mensaje.isEmpty()) {
				List<MensajeDto> msjList = new ArrayList<>();
				DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

				for (Object[] row : mensaje) {
					MensajeDto mensajeDto = new MensajeDto();
					mensajeDto.setUsername((String) row[0]);
					LocalDateTime fecha = (LocalDateTime) row[1];
					mensajeDto.setFecha(fecha.format(fechaFormatter));
					mensajeDto.setHora(fecha.format(horaFormatter));
					mensajeDto.setMensaje((String) row[2]);
					msjList.add(mensajeDto);
				}

				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writeValueAsString(msjList);
				return ResponseEntity.ok(json);
			}
			return new ResponseEntity<>("No existe el usuario", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al recuperar los mensajes", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
