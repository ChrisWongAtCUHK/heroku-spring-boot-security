package com.heroku.service.jwt;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.heroku.model.jwt.LoginRequest;
import com.heroku.model.jwt.LoginResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class TokenService {
  private Key secretKey;

  @PostConstruct
  private void init() {
    String key = "VincentIsParticipatingItHomeIronmanContest";
    secretKey = Keys.hmacShaKeyFor(key.getBytes());
  }

  public LoginResponse createToken(LoginRequest request) {
    String accessToken = createAccessToken(request.getUsername());

    LoginResponse res = new LoginResponse();
    res.setAccessToken(accessToken);

    return res;
  }

  private String createAccessToken(String username) {
    // 有效時間（毫秒）
    long expirationMillis = Instant.now()
        .plusSeconds(90)
        .getEpochSecond()
        * 1000;

    // 設置標準內容與自定義內容
    Claims claims = Jwts.claims();
    claims.setSubject("Access Token");
    claims.setIssuedAt(new Date());
    claims.setExpiration(new Date(expirationMillis));
    claims.put("username", username);

    // 簽名後產生 token
    return Jwts.builder()
        .setClaims(claims)
        .signWith(secretKey)
        .compact();
  }
}