package ru.korolev.springboot.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.korolev.springboot.model.dto.UserDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RestServiceImpl implements RestService {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;

    public RestServiceImpl(OAuth2AuthorizedClientService authorizedClientService, RestTemplateBuilder restTemplateBuilder) {
        this.authorizedClientService = authorizedClientService;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public Optional<UserDTO> authorization(String login, String password) {
        init(login, password);

        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                    "http://localhost:8081/api/v1/authorization?login={login}&password={password}&social={social}&name={name}",
                    UserDTO.class,
                    login,
                    password,
                    false,
                    ""
            );

            return response.getStatusCode() == HttpStatus.OK
                    ? Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (HttpStatusCodeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDTO> authorization(boolean social, String login, String name) {
        init();

        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                    "http://localhost:8081/api/v1/authorization?login={login}&password={password}&social={social}&name={name}",
                    UserDTO.class,
                    login,
                    "",
                    social,
                    name
            );

            return response.getStatusCode() == HttpStatus.OK
                    ? Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (HttpStatusCodeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserDTO> getAll() {
        try {
            ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(
                    "http://localhost:8081/api/v1/user/all",
                    UserDTO[].class
            );

            if (response.getBody() == null) {
                return Collections.emptyList();
            }

            return response.getStatusCode() == HttpStatus.OK
                    ? Arrays.asList(response.getBody()) : Collections.emptyList();
        } catch (HttpStatusCodeException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<UserDTO> getById(Long id) {
        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                    "http://localhost:8081/api/v1/user/{id}",
                    UserDTO.class,
                    id
            );
            return response.getStatusCode() == HttpStatus.OK
                    ? Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (HttpStatusCodeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDTO> create(UserDTO userDTO) {
        try {
            ResponseEntity<UserDTO> response = restTemplate.postForEntity(
                    "http://localhost:8081/api/v1/user/create",
                    userDTO,
                    UserDTO.class
            );
            return response.getStatusCode() == HttpStatus.OK
                    ? Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (HttpStatusCodeException e) {
            return Optional.empty();
        }
    }

    @Override
    public HttpStatus update(UserDTO userDTO) {
        try {
            ResponseEntity<?> response = restTemplate.postForEntity(
                    "http://localhost:8081/api/v1/user/update",
                    userDTO,
                    UserDTO.class
            );

            return response.getStatusCode();
        } catch (HttpStatusCodeException e) {
            return e.getStatusCode();
        }
    }

    @Override
    public HttpStatus delete(Long id) {
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(
                    "http://localhost:8081/api/v1/user/delete/{id}",
                    ResponseEntity.class,
                    id
            );

            return response.getStatusCode();
        } catch (HttpStatusCodeException e) {
            return e.getStatusCode();
        }
    }

    @Override
    public void setBasicAuthentication(String login, String password) {
        init(login, password);
    }

    private void init(String login, String password) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication(login, password)
                .build();
    }

    private void init() {
        this.restTemplate = restTemplateBuilder.build();
    }
}