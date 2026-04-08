package com.heroku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heroku.UserIdentity;
import com.heroku.model.AppUser;
import com.heroku.model.UserAuthority;
import com.heroku.repository.UserRepository;

@RestController
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserIdentity userIdentity;

  @PostMapping("/users")
  public ResponseEntity<Void> createUser(@RequestBody AppUser user) {
    if (userIdentity.isPremium()) {
      user.setCreatorId(userIdentity.getId());
    }

    userRepository.insert(user);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    AppUser deletingUser = userRepository.findById(id);
        String myUserId = userIdentity.getId();
        UserAuthority myAuthority = userIdentity.getUserAuthority();

        if (id.equals(myUserId) ||
            myUserId.equals(deletingUser.getCreatorId()) ||
            myAuthority == UserAuthority.ADMIN) {

            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
