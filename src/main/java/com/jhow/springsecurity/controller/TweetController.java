package com.jhow.springsecurity.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jhow.springsecurity.controller.dto.CreateTweetDTO;
import com.jhow.springsecurity.entities.Role;
import com.jhow.springsecurity.entities.Tweet;
import com.jhow.springsecurity.entities.User;
import com.jhow.springsecurity.repositories.TweetRepository;
import com.jhow.springsecurity.repositories.UserRepository;

@RestController
@RequestMapping("/tweets")
public class TweetController {
  private final TweetRepository tweetRepository;
  private final UserRepository userRepository;

  public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
    this.tweetRepository = tweetRepository;
    this.userRepository = userRepository;
  }

  @PostMapping()
  public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDTO dto, JwtAuthenticationToken token) {
    Optional<User> user = userRepository.findById(UUID.fromString(token.getName()));
    Tweet tweet = new Tweet();
    tweet.setUser(user.get());
    tweet.setContent(dto.content());

    tweetRepository.save(tweet);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token) {

    Tweet tweet = tweetRepository.findById(tweetId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    Optional<User> user = userRepository.findById(UUID.fromString(token.getName()));

    boolean isAdmin = user.get().getRoles().stream()
        .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

    if (isAdmin || tweet.getUser().getUserID().equals(UUID.fromString(token.getName()))) {
      tweetRepository.deleteById(tweetId);
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    tweetRepository.deleteById(tweetId);

    return ResponseEntity.ok().build();
  }
}
