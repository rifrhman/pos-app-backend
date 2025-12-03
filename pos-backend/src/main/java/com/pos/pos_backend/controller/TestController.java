package com.pos.pos_backend.controller;

import com.pos.pos_backend.entity.User;
import com.pos.pos_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/test")
public class TestController {

  private final UserRepository userRepository;

  @PostMapping("/add")
  public User add(@RequestBody User user){
    return userRepository.save(user);
  }

}
