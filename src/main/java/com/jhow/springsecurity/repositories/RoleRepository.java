package com.jhow.springsecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhow.springsecurity.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
