package com.example.librarymanagement.utils;

import com.example.librarymanagement.entity.User;
import com.example.librarymanagement.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("#{${jwt.max-token-interval-hour}*60*60*1000}")
    private long JWT_TOKEN_VALIDITY;


    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getOidFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("oid", String.class));
    }

    public User.Role getRoleFromToken(String token) {
        String roleString = getClaimFromToken(token, claims -> claims.get("role", String.class));
        return User.Role.valueOf(roleString);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", userDetails.getName());
        claims.put("oid", userDetails.getOid());
        claims.put("role", userDetails.getRole());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", userDetails.getOid());
        return Jwts.builder().setIssuedAt(new Date(System.currentTimeMillis()))
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .setIssuer("http://library-project.com")
                .signWith(signatureAlgorithm, SECRET_KEY).compact();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setHeaderParam("typ", "JWT").
                setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .setIssuer("http://library-project.com")
                .signWith(signatureAlgorithm, SECRET_KEY).compact();
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final String oid = getOidFromToken(token);
        User.Role role = getRoleFromToken(token);
        return userRepository.existsByOidAndUsernameAndRole(oid,username, role) && !isTokenExpired(token) && (userDetails != null);
    }

    public Boolean validateRefreshToken(String token) {
        final String oid = getOidFromToken(token);
        return userRepository.existsByOid(oid) && !isTokenExpired(token);
    }

}
