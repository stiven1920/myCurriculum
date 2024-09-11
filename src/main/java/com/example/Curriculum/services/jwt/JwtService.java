package com.example.Curriculum.services.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.Curriculum.models.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
//	@Value("${security.jwt.expiration-minutes}")
	private int EXPIRATION_MINUTES = 60;

//	@Value("${security.jwt.secret-key}")
	private String SECRET_KEY = "VABvAGQAbwAgAEUAcwBwAG8AcwBpAGIAbABlACAAMQAzADIAMQAyADIA";

	public String generateToken(Usuario user, Map<String, Object> extraClaims) {

		Date issuedAt = new Date(System.currentTimeMillis());
		Date expiration = new Date(issuedAt.getTime() + (EXPIRATION_MINUTES * 60 * 1000));

		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(user.getUsername())
				.setIssuedAt(issuedAt)
				.setExpiration(expiration)
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.signWith(generateKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	private Key generateKey() {
		byte[] secretAsBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(secretAsBytes);
	}

	public String extractUsername(String jwt) {
		return extractAllClaims(jwt).getSubject();
	}

	private Claims extractAllClaims(String jwt) {
		return Jwts.parserBuilder().setSigningKey(generateKey()).build().parseClaimsJws(jwt).getBody();
	}
}
