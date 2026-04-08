package com.heroku.service.jwt;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.heroku.model.AppUserDetails;
import com.heroku.model.jwt.LoginRequest;
import com.heroku.model.jwt.LoginResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class TokenService {
  private Key secretKey;
  private JwtParser jwtParser;
  @Autowired
  private AuthenticationProvider authenticationProvider;

  @PostConstruct
  private void init() {
    String key = "VincentIsParticipatingItHomeIronmanContest";
    secretKey = Keys.hmacShaKeyFor(key.getBytes());

    jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
  }

  public LoginResponse createToken(LoginRequest request) {
    // 封裝帳密
    Authentication authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

    // 執行帳密認證
    authToken = authenticationProvider.authenticate(authToken);

    // 以自定義的 UserDetails 接收已認證的使用者
    AppUserDetails userDetails = (AppUserDetails) authToken.getPrincipal();

    // 產生 token
    String accessToken = createAccessToken(userDetails.getUsername());
    String refreshToken = createRefreshToken(userDetails.getUsername());

    LoginResponse res = new LoginResponse();
    res.setAccessToken(accessToken);
    res.setRefreshToken(refreshToken);
    res.setUserId(userDetails.getId());
    res.setEmail(userDetails.getUsername());
    res.setUserAuthority(userDetails.getUserAuthority());
    res.setPremium(userDetails.isPremium());
    res.setTrailExpiration(userDetails.getTrailExpiration());

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

  private String createRefreshToken(String username) {
    long expirationMillis = Instant.now()
        .plusSeconds(600)
        .getEpochSecond()
        * 1000;

    Claims claims = Jwts.claims();
    claims.setSubject("Refresh Token");
    claims.setIssuedAt(new Date());
    claims.setExpiration(new Date(expirationMillis));
    claims.put("username", username);

    return Jwts.builder()
        .setClaims(claims)
        .signWith(secretKey)
        .compact();
  }

  public Map<String, Object> parseToken(String token) {
    Claims claims = jwtParser.parseClaimsJws(token).getBody();
    return new HashMap<>(claims);
  }

  public String refreshAccessToken(String refreshToken) {
    Map<String, Object> payload = parseToken(refreshToken);
    String username = (String) payload.get("username");

    return createAccessToken(username);
  }
}