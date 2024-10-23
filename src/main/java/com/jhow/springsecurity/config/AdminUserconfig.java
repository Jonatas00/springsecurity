package com.jhow.springsecurity.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.jhow.springsecurity.entities.Role;
import com.jhow.springsecurity.entities.User;
import com.jhow.springsecurity.repositories.RoleRepository;
import com.jhow.springsecurity.repositories.UserRepository;

@Configuration
public class AdminUserconfig implements CommandLineRunner {
  private RoleRepository roleRepository;
  private UserRepository userRepository;
  private BCryptPasswordEncoder passwordEncoder;

  public AdminUserconfig(
      RoleRepository roleRepository,
      UserRepository userRepository,
      BCryptPasswordEncoder passwordEncoder) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

    var userAdmin = userRepository.FindByUsername("admin");

    userAdmin.ifPresentOrElse(
        (user) -> {
          System.out.println("admin já existe");
        },
        () -> {
          var user = new User();
          user.setUsername("admin");
          user.setPassword(passwordEncoder.encode("123"));
          user.setRoles(Set.of(roleAdmin));
          userRepository.save(user);
        });
  }

}