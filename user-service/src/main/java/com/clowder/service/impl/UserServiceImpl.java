package com.clowder.service.impl;

import com.clowder.exception.UserException;
import com.clowder.model.User;
import com.clowder.repository.UserRepository;
import com.clowder.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserException("User not found with id: " + id));
  }

  @Override
  public User updateUser(Long id, User user) {
    User existing = userRepository.findById(id)
        .orElseThrow(() -> new UserException("User not found with id: " + id));

    existing.setFullName(user.getFullName());
    existing.setUsername(user.getUsername());
    existing.setEmail(user.getEmail());
    existing.setPassword(user.getPassword());
    existing.setPhone(user.getPhone());
    existing.setRole(user.getRole());

    return userRepository.save(existing);
  }

  @Override
  public void deleteUser(Long id){
     if (!userRepository.existsById(id)) {
      throw new UserException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }
}

