package com.looselytyped.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.looselytyped.persistence.model.User;
import com.looselytyped.persistence.repo.UserRepository;

@RestController // This means that this class is a Controller
@RequestMapping("/api/users")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private Environment environment;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@RequestBody User user) {
    return userRepository.save(user);
  }

  @GetMapping
  public Iterable<User> findAll() {
    for (String profileName : environment.getActiveProfiles()) {
      System.out.println("Currently active profile: " + profileName);
    }
    return userRepository.findAll();
  }
}
