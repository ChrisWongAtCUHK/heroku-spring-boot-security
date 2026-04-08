package com.heroku.controller.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
}
