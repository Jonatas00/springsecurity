package com.jhow.springsecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhow.springsecurity.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
