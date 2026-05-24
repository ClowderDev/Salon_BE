package com.clowder.booking.service;

import com.clowder.booking.model.User;
import java.util.List;

public interface UserService {
  User createUser(User user);

  List<User> getUsers();

  User getUserById(Long id);

  User updateUser(Long id, User user);

  void deleteUser(Long id);

  User getUserFromJwt(String token);
}
