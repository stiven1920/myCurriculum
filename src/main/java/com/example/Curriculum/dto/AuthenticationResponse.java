package com.example.Curriculum.dto;

import com.example.Curriculum.models.Usuario;

public class AuthenticationResponse {
	private String jwt;
	private Usuario user;

	public AuthenticationResponse(String jwt, Usuario user) {
		this.jwt = jwt;
		this.user = user;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}
}
