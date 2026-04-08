package com.heroku.controller.jwt;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.heroku.model.jwt.LoginRequest;
import com.heroku.model.jwt.LoginResponse;
import com.heroku.service.jwt.TokenService;

@RestController
public class JWTController {
  @Autowired
  private TokenService tokenService;

  @PostMapping("/auth/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    LoginResponse res = tokenService.createToken(request);
    return ResponseEntity.ok(res);
  }

  @GetMapping("/parse-token")
  public ResponseEntity<Map<String, Object>> parseToken(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
    Map<String, Object> jwtPayload = tokenService.parseToken(authorization);
    return ResponseEntity.ok(jwtPayload);
  }

  @PostMapping("/auth/refresh-token")
  public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> request) {
    String refreshToken = request.get("refreshToken");
    String accessToken = tokenService.refreshAccessToken(refreshToken);
    Map<String, String> res = Map.of("accessToken", accessToken);

    return ResponseEntity.ok(res);
  }
}
