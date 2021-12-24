package com.team.proman.component;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtToken implements Serializable {

	private static final long serialVersionUID = -6134476442211712383L;

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.token.validity}")
	private long token_validity;

	/**
	 * Get username from token.
	 * 
	 * @param token
	 * @return username from token
	 * */
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * Get expiration date from token.
	 * 
	 * @param token
	 * @return expiration date from token
	 * */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	/**
	 * Get claim from token.
	 * 
	 * @param token
	 * @param claimsResolver
	 * @return claim object
	 * */	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Get claims from token.
	 * 
	 * @param token
	 * @return claim objects
	 * */	
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * Check if token is expired.
	 * 
	 * @param token
	 * @return true if token isn't expired otherwise false
	 * */	
	private Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	/**
	 * Generate a new token.
	 * 
	 * @param userDetails
	 * @return new token
	 * */	
	public String generateToken(UserDetails userDetails) {
		String subject = userDetails.getUsername();
		String authorities = userDetails.getAuthorities()
				.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

		return Jwts.builder()
				.claim("roles", authorities)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + token_validity * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * Validate a token.
	 * 
	 * @param token
	 * @return true if token is validate otherwise false
	 * */	
	public Boolean validateToken(String token, UserDetails userDetails) {
		String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
