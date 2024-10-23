package com.jhow.springsecurity.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jhow.springsecurity.controller.dto.LoginRequest;
import com.jhow.springsecurity.controller.dto.LoginResponse;
import com.jhow.springsecurity.repositories.UserRepository;

@RestController
public class TokenController {
  private final JwtEncoder jwtEncoder;
  private final UserRepository userRepository;
  private BCryptPasswordEncoder passwordEncoder;

  public TokenController(
      JwtEncoder jwtEncoder,
      UserRepository userRepository,
      BCryptPasswordEncoder passwordEncoder) {
    this.jwtEncoder = jwtEncoder;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    var user = userRepository.findByUsername(loginRequest.username());

    if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
      throw new BadCredentialsException("user or password is invalid!");
    }

    Instant now = Instant.now();
    Long expiresIn = 300L;

    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("mybackend")
        .subject(user.get().getUserID().toString())
        .issuedAt(now)
        .expiresAt(now.plusSeconds(expiresIn))
        .build();

    String jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
  }

}
