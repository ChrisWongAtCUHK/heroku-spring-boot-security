package com.heroku.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.heroku.model.AppUser;

import jakarta.annotation.PostConstruct;

@Repository
public class UserRepository {
  private final Map<String, AppUser> idToUserMap = new HashMap<>();

  @PostConstruct
  private void init() {
    var adminUser = AppUser.getTestAdminUser();
    var normalUser = AppUser.getTestNormalUser();
    var guestUser = AppUser.getTestGuestUser();

    idToUserMap.put(adminUser.getId(), adminUser);
    idToUserMap.put(normalUser.getId(), normalUser);
    idToUserMap.put(guestUser.getId(), guestUser);
  }

  public AppUser findById(String id) {
    return idToUserMap.get(id);
  }

  public AppUser findByEmail(String email) {
    return idToUserMap.values().stream()
        .filter(u -> u.getEmail().equals(email))
        .findFirst()
        .orElse(null);
  }

  public List<AppUser> findAll() {
    return new ArrayList<>(idToUserMap.values());
  }

  public void insert(AppUser user) {
    idToUserMap.put(user.getId(), user);
  }
}