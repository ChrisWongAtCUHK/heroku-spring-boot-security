package com.heroku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.heroku.model.AppUser;
import com.heroku.model.AppUserDetails;
import com.heroku.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public AppUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser appUser = userRepository.findByEmail(username);
    if (appUser == null) {
      throw new UsernameNotFoundException("Can't find user: " + username);
    }

    return new AppUserDetails(appUser);
  }
}
