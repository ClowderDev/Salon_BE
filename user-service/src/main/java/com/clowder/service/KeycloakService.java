package com.clowder.service;

import com.clowder.dto.request.Credential;
import com.clowder.dto.request.KeycloakRole;
import com.clowder.dto.request.KeycloakUserDTO;
import com.clowder.dto.request.SignUpDTO;
import com.clowder.dto.request.TokenResponse;
import com.clowder.dto.request.UserRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class KeycloakService {

  private static final String KEYCLOAK_BASE_URL = "http://localhost:8080";
  private static final String KEYCLOAK_AMDIN_API = KEYCLOAK_BASE_URL + "/admin/realms/master/users";
  private static final String TOKEN_URL =
      KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/token";
  private static final String CLIENT_ID = "salon-client";
  private static final String CLIENT_SECRET = "Z3cH23EFz7SRx09D60YU6pEnWxKtbI9L";
  private static final String GRANT_TYPE = "password";
  private static final String scope = "openid profile email";
  private static final String username = "clowderline";
  private static final String password = "123456";
  private static final String clientId = "a7c65638-49c5-4e12-a98e-1ddc94b60188";

  private final RestTemplate restTemplate;

  public void createUser(SignUpDTO signUpDTO) {

    String ACCESS_TOKEN =
        getAdminAccessToken(username, password, GRANT_TYPE, null).getAccessToken();

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
    headers.setBearerAuth(ACCESS_TOKEN);

    HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);

    ResponseEntity<String> response =
        restTemplate.exchange(KEYCLOAK_AMDIN_API, HttpMethod.POST, request, String.class);

    if (response.getStatusCode() == HttpStatus.CREATED) {
      System.out.println("User created successfully in Keycloak");

      KeycloakUserDTO user = fetchFirstUserByUsername(signUpDTO.getUsername(), ACCESS_TOKEN);

      KeycloakRole role = getRoleByName(clientId, ACCESS_TOKEN, signUpDTO.getRole().toString());

      List<KeycloakRole> roles = new ArrayList<>();
      roles.add(role);

      assignRoleToUser(user.getId(), clientId, roles, ACCESS_TOKEN);
    } else {
      System.out.println("Failed to create user in Keycloak: " + response.getBody());
      throw new RuntimeException("Failed to create user in Keycloak: " + response.getBody());
    }
  }

  public TokenResponse getAdminAccessToken(
      String username, String password, String grantType, String refreshToken) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", grantType);
    requestBody.add("username", username);
    requestBody.add("password", password);
    requestBody.add("refresh_token", refreshToken);
    requestBody.add("client_id", clientId);
    requestBody.add("client_secret", CLIENT_SECRET);
    requestBody.add("scope", scope);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

    ResponseEntity<TokenResponse> response =
        restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, TokenResponse.class);

    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
      return response.getBody();
    }
    throw new RuntimeException(
        "Failed to obtain access token from Keycloak: " + response.getBody());
  }

  public KeycloakRole getRoleByName(String clientId, String token, String role) {

    String url = KEYCLOAK_BASE_URL + "/admin/realms/naster/clients/" + clientId + "/roles/" + role;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Void> request = new HttpEntity<>(headers);

    ResponseEntity<KeycloakRole> response =
        restTemplate.exchange(url, HttpMethod.GET, request, KeycloakRole.class);

    if (response.getBody() != null) {
      return response.getBody();
    }
    throw new RuntimeException("Failed to get role from keycloak: " + response.getBody());
  }

  public KeycloakUserDTO fetchFirstUserByUsername(String username, String token) {
    String url = KEYCLOAK_BASE_URL + "/admin/realms/naster/users?username=" + username;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<KeycloakUserDTO[]> response =
        restTemplate.exchange(url, HttpMethod.GET, request, KeycloakUserDTO[].class);

    KeycloakUserDTO[] users = response.getBody();
    if (users != null || users.length > 0) {
      return users[0];
    }

    throw new RuntimeException("Failed to fetch user from keycloak: " + response.getBody());
  }

  public void assignRoleToUser(
      String userId, String clientId, List<KeycloakRole> roles, String token) {

    String url =
        KEYCLOAK_BASE_URL
            + "/admin/realms/naster/users/"
            + userId
            + "/role-mappings/clients/"
            + clientId;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<List<KeycloakRole>> request = new HttpEntity<>(roles, headers);

    try {
      ResponseEntity<String> response =
          restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to assign role to user: " + e.getLocalizedMessage());
    }
  }

  public KeycloakUserDTO fetchUserProfileByJwt(String token) {

    String url = KEYCLOAK_BASE_URL + "/admin/realms/master/protocol/openid-connect/userinfo";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<>(headers);

    try {
      ResponseEntity<KeycloakUserDTO> response =
          restTemplate.exchange(url, HttpMethod.GET, request, KeycloakUserDTO.class);
      return response.getBody();
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to fetch user profile from keycloak: " + e.getLocalizedMessage());
    }
  }
}
