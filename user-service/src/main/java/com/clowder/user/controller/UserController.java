package com.clowder.user.controller;

import com.clowder.user.model.User;
import com.clowder.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.getUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> getUserById(@PathVariable Long userId) {
    User user = userService.getUserById(userId);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
    User createdUser = userService.createUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @GetMapping("/profiles")
  public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) {
    User user = userService.getUserFromJwt(jwt);
    return ResponseEntity.ok(user);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<User> updateUser(
      @PathVariable("userId") Long userId, @RequestBody @Valid User user) {
    user.setId(userId);
    User updatedUser = userService.updateUser(userId, user);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok("User deleted");
  }
}
