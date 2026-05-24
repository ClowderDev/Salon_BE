package com.clowder.user.service;

import com.clowder.user.dto.request.Credential;
import com.clowder.user.dto.request.KeycloakRole;
import com.clowder.user.dto.request.KeycloakUserDTO;
import com.clowder.user.dto.request.SignUpDTO;
import com.clowder.user.dto.request.UserRequest;
import com.clowder.user.dto.response.TokenResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@RefreshScope
public class KeycloakService {

  @Value("${keycloak.base-url}")
  private String keycloakBaseUrl;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.client-id}")
  private String clientId;

  @Value("${keycloak.client-secret}")
  private String clientSecret;

  @Value("${keycloak.admin.username}")
  private String adminUsername;

  @Value("${keycloak.admin.password}")
  private String adminPassword;

  @Value("${keycloak.scope}")
  private String scope;

  @Value("${keycloak.internal-client-id}")
  private String internalClientId;

  private final RestTemplate restTemplate;

  public void createUser(SignUpDTO signUpDTO) {
    String accessToken =
        getAdminAccessToken(adminUsername, adminPassword, "password", null).getAccessToken();

    Credential credential = new Credential();
    credential.setTemporary(false);
    credential.setType("password");
    credential.setValue(signUpDTO.getPassword());

    UserRequest userRequest = new UserRequest();
    userRequest.setUsername(signUpDTO.getUsername());
    userRequest.setEmail(signUpDTO.getEmail());
    userRequest.setEnabled(true);
    userRequest.setFirstName(signUpDTO.getFirstName());
    userRequest.setLastName(signUpDTO.getLastName());
    userRequest.getCredentials().add(credential);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(accessToken);

    HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);

    String adminApiUrl = keycloakBaseUrl + "/admin/realms/" + realm + "/users";
    ResponseEntity<String> response =
        restTemplate.exchange(adminApiUrl, HttpMethod.POST, request, String.class);

    if (response.getStatusCode() == HttpStatus.CREATED) {
      log.info("User created successfully in Keycloak: {}", signUpDTO.getUsername());

      KeycloakUserDTO user = fetchFirstUserByUsername(signUpDTO.getUsername(), accessToken);

      KeycloakRole role =
          getRoleByName(internalClientId, accessToken, signUpDTO.getRole().toString());

      List<KeycloakRole> roles = new ArrayList<>();
      roles.add(role);

      assignRoleToUser(user.getId(), internalClientId, roles, accessToken);
    } else {
      log.error("Failed to create user in Keycloak: {}", response.getBody());
      throw new RuntimeException("Failed to create user in Keycloak: " + response.getBody());
    }
  }

  public TokenResponse getAdminAccessToken(
      String username, String password, String grantType, String refreshToken) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", grantType);
    requestBody.add("username", username);
    requestBody.add("password", password);
    requestBody.add("refresh_token", refreshToken);
    requestBody.add("client_id", clientId);
    requestBody.add("client_secret", clientSecret);
    requestBody.add("scope", scope);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

    String tokenUrl = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/token";
    ResponseEntity<TokenResponse> response =
        restTemplate.exchange(tokenUrl, HttpMethod.POST, request, TokenResponse.class);

    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
      return response.getBody();
    }
    throw new RuntimeException(
        "Failed to obtain access token from Keycloak: " + response.getBody());
  }

  public KeycloakRole getRoleByName(String clientId, String token, String role) {
    String url =
        keycloakBaseUrl + "/admin/realms/" + realm + "/clients/" + clientId + "/roles/" + role;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Void> request = new HttpEntity<>(headers);

    ResponseEntity<KeycloakRole> response =
        restTemplate.exchange(url, HttpMethod.GET, request, KeycloakRole.class);

    if (response.getBody() != null) {
      return response.getBody();
    }
    throw new RuntimeException("Failed to get role from Keycloak: " + response.getBody());
  }

  public KeycloakUserDTO fetchFirstUserByUsername(String username, String token) {
    String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users?username=" + username;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<KeycloakUserDTO[]> response =
        restTemplate.exchange(url, HttpMethod.GET, request, KeycloakUserDTO[].class);

    KeycloakUserDTO[] users = response.getBody();
    if (users != null && users.length > 0) {
      return users[0];
    }

    throw new RuntimeException("Failed to fetch user from Keycloak for username: " + username);
  }

  public void assignRoleToUser(
      String userId, String clientId, List<KeycloakRole> roles, String token) {

    String url =
        keycloakBaseUrl
            + "/admin/realms/"
            + realm
            + "/users/"
            + userId
            + "/role-mappings/clients/"
            + clientId;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<List<KeycloakRole>> request = new HttpEntity<>(roles, headers);

    try {
      restTemplate.exchange(url, HttpMethod.POST, request, String.class);
      log.info("Role assigned successfully to user: {}", userId);
    } catch (Exception e) {
      log.error("Failed to assign role to user: {}", userId, e);
      throw new RuntimeException("Failed to assign role to user: " + e.getLocalizedMessage());
    }
  }

  public KeycloakUserDTO fetchUserProfileByJwt(String token) {
    String url = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<>(headers);

    try {
      ResponseEntity<KeycloakUserDTO> response =
          restTemplate.exchange(url, HttpMethod.GET, request, KeycloakUserDTO.class);
      return response.getBody();
    } catch (Exception e) {
      log.error("Failed to fetch user profile from Keycloak", e);
      throw new RuntimeException(
          "Failed to fetch user profile from Keycloak: " + e.getLocalizedMessage());
    }
  }
}
