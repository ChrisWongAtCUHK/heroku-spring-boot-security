package com.heroku.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.heroku.model.AppUser;
import com.heroku.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser appUser = userRepository.findByEmail(username);
    if (appUser == null) {
      throw new UsernameNotFoundException("Can't find user: " + username);
    }

    return new User(appUser.getEmail(), appUser.getPassword(), List.of(appUser.getAuthority()));
  }
}
