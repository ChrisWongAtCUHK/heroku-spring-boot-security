package com.heroku;

import com.heroku.service.jwt.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UserDetailsService userDetailsService;

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // 取得 request header 的值
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (bearerToken != null) {
      String accessToken = bearerToken.replace("Bearer ", "");

      // 解析 token
      Map<String, Object> tokenPayload = tokenService.parseToken(accessToken);
      String username = (String) tokenPayload.get("username");

      // 查詢使用者
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // 將使用者身份與權限傳遞給 Spring Security
      Authentication authentication = new UsernamePasswordAuthenticationToken(
          userDetails,
          userDetails.getPassword(),
          userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 將 request 送往 controller 或下一個 filter
    filterChain.doFilter(request, response);
  }
}
