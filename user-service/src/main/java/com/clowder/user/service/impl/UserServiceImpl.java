package com.clowder.user.service.impl;

import com.clowder.user.dto.request.KeycloakUserDTO;
import com.clowder.user.exception.ResourceNotFoundException;
import com.clowder.user.model.User;
import com.clowder.user.repository.UserRepository;
import com.clowder.user.service.KeycloakService;
import com.clowder.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final KeycloakService keycloakService;

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
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  @Override
  public User updateUser(Long id, User user) {
    User existing =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    existing.setFullName(user.getFullName());
    existing.setUsername(user.getUsername());
    existing.setEmail(user.getEmail());
    existing.setPassword(user.getPassword());
    existing.setPhone(user.getPhone());
    existing.setRole(user.getRole());

    return userRepository.save(existing);
  }

  @Override
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }

  @Override
  public User getUserFromJwt(String token) {
    KeycloakUserDTO keycloakUserDTO = keycloakService.fetchUserProfileByJwt(token);
    return userRepository.findByEmail(keycloakUserDTO.getEmail());
  }
}
