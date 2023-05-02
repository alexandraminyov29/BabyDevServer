package com.babydev.app.security.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY = "6A586E3272357538782F413F4428472B4B61"
			+ "50645367566B5970337336763979244226452948404D6351655468576D5A7134743"
			+ "777217A25432A462D4A614E645267556A586E3272357538782F413F4428472B4B62"
			+ "50655368566D5970337336763979244226452948404D635166546A576E5A7234743"
			+ "777217A25432A462D4A";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken(Map<String, Object> extraClaims,
			UserDetails userDetails) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {	
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {
		final byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	/**
	 *  Method that extracts the user from the token
	 *  
	 *  @param header the header that contains the email
	 */
	public String extractUsernameFromToken(String header) {
		final String token = header.substring("Bearer ".length());
		return extractUsername(token);
	}

}
