package com.heroku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heroku.model.AppUser;
import com.heroku.repository.UserRepository;

@RestController
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @PostMapping("/users")
  public ResponseEntity<Void> createUser(@RequestBody AppUser user) {
    userRepository.insert(user);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/users")
  public ResponseEntity<List<AppUser>> getAllUsers() {
    var users = userRepository.findAll();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<AppUser> getUser(@PathVariable String id) {
    var user = userRepository.findById(id);
    return user != null
        ? ResponseEntity.ok(user)
        : ResponseEntity.notFound().build();
  }
}
