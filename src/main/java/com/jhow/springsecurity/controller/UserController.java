package com.jhow.springsecurity.controller;

import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jhow.springsecurity.controller.dto.CreateUserDTO;
import com.jhow.springsecurity.entities.Role;
import com.jhow.springsecurity.entities.User;
import com.jhow.springsecurity.repositories.RoleRepository;
import com.jhow.springsecurity.repositories.UserRepository;

import jakarta.transaction.Transactional;

@RestController
public class UserController {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserController(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  @Transactional
  @PostMapping("/users")
  public ResponseEntity<Void> newUser(@RequestBody CreateUserDTO dto) {

    Role basicRole = roleRepository.findByName(Role.Values.BASIC.name());

    Optional<User> userFromDB = userRepository.findByUsername(dto.username());
    if (userFromDB.isPresent()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var user = new User();
    user.setUsername(dto.username());
    user.setPassword(passwordEncoder.encode(dto.password()));
    user.setRoles(Set.of(basicRole));

    userRepository.save(user);

    return ResponseEntity.ok().build();
  }

}
