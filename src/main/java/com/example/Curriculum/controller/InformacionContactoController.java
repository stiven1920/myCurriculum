package com.example.Curriculum.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Curriculum.models.InformacionContacto;
import com.example.Curriculum.services.InformacionContactoServices;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/contact")
public class InformacionContactoController {

	@Autowired
	private InformacionContactoServices infoContServices;

	@Autowired
	private JavaMailSender javaSender;

	@Value(value = "${spring.mail.username}")
	private String emailTo;

	@PostMapping(path = "/sendMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveInfoContc(@RequestBody InformacionContacto infoCont) {

		if (infoCont == null || infoCont.getCorreo() == null || infoCont.getCorreo().isEmpty()) {
			return createErrorResponse(HttpStatus.BAD_REQUEST, "La información de contacto o el correo son inválidos.");
		}

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(emailTo);
			message.setSubject("Mi Nombre es " + infoCont.getNombre() + " y quiero contactar contigo");
			message.setText(infoCont.getMensaje() + " el correo con que te quiero contacta es: " + infoCont.getCorreo());
			javaSender.send(message);
			InformacionContacto infoSave = infoContServices.saveContato(infoCont);

			Map<String, Object> successResponse = new HashMap<>();
			successResponse.put("message", "Fue mensaje Fue enviado");
			successResponse.put("data", infoSave);
			return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);

		} catch (Exception e) {
			return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al procesar la solicitud",
					e.getMessage());
		}
	}

	private ResponseEntity<Object> createErrorResponse(HttpStatus status, String message) {
		return createErrorResponse(status, message, null);
	}

	private ResponseEntity<Object> createErrorResponse(HttpStatus status, String message, String details) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("error", message);
		if (details != null) {
			errorResponse.put("details", details);
		}
		return ResponseEntity.status(status).body(errorResponse);
	}
}
