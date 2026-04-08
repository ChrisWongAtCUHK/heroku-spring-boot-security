package com.heroku;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.heroku.model.AppUser;
import com.heroku.model.AppUserDetails;
import com.heroku.model.UserAuthority;

@Component
public class UserIdentity {

  private AppUserDetails getUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();

    return "anonymousUser".equals(principal)
        ? new AppUserDetails(new AppUser())
        : (AppUserDetails) principal;
  }

  public String getId() {
    return getUserDetails().getId();
  }

  public String getUsername() {
    return getUserDetails().getUsername();
  }

  public UserAuthority getUserAuthority() {
    return getUserDetails().getUserAuthority();
  }

  public boolean isPremium() {
    return getUserDetails().isPremium();
  }
}
