package com.br.RestAll.comum.security;

import com.br.RestAll.usuario.entity.Perfil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final long EXPIRATION_TIME = 86400000; // 24 horas

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String gerarToken(UsuarioDetails usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("perfil", usuario.getPerfil().name());
        if (usuario.getRestauranteId() != null) {
            claims.put("restauranteId", usuario.getRestauranteId());
        }

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public String extrairUsername(String token) {
        return extrairAllClaims(token).getSubject();
    }

    public Long extrairUsuarioId(String token) {
        return extrairAllClaims(token).get("id", Long.class);
    }

    public Perfil extrairPerfil(String token) {
        return Perfil.valueOf(extrairAllClaims(token).get("perfil", String.class));
    }

    public Long extrairRestauranteId(String token) {
        return extrairAllClaims(token).get("restauranteId", Long.class);
    }

    public boolean isTokenValido(String token, UsuarioDetails usuarioDetails) {
        final String username = extrairUsername(token);
        return (username.equals(usuarioDetails.getUsername()) && !isTokenExpirado(token));
    }

    private boolean isTokenExpirado(String token) {
        return extrairAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extrairAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
