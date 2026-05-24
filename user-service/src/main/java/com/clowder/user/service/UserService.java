package com.clowder.user.service;

import com.clowder.user.model.User;
import java.util.List;

public interface UserService {
  User createUser(User user);

  List<User> getUsers();

  User getUserById(Long id);

  User updateUser(Long id, User user);

  void deleteUser(Long id);

  User getUserFromJwt(String token);
}
