package ru.korolev.springboot.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.korolev.springboot.model.dto.UserDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RestServiceImpl implements RestService {

    private RestTemplate restTemplate;

    private final RestTemplateBuilder restTemplateBuilder;

    public RestServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    public void init(String login, String password) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication(login, password)
                .build();
    }

    @Override
    public Optional<UserDTO> authorization(String login, String password) {
        init(login, password);

        ResponseEntity<UserDTO> responseEntity = restTemplate.getForEntity(
                "http://localhost:8081/api/v1/authorization?login={login}&password={password}",
                UserDTO.class,
                login,
                password
        );

        return responseEntity.getStatusCode() == HttpStatus.OK
                ? Optional.ofNullable(responseEntity.getBody()) : Optional.empty();
    }

    @Override
    public List<UserDTO> getAll() {
        ResponseEntity<UserDTO[]> responseEntity = restTemplate.getForEntity(
                "http://localhost:8081/api/v1/user/all",
                UserDTO[].class
        );

        if (responseEntity.getBody() == null) {
            return Collections.emptyList();
        }

        return responseEntity.getStatusCode() == HttpStatus.OK
                ? Arrays.asList(responseEntity.getBody()) : Collections.emptyList();
    }

    @Override
    public Optional<UserDTO> getById(Long id) {
        ResponseEntity<UserDTO> responseEntity = restTemplate.getForEntity(
                "http://localhost:8081/api/v1/user/{id}",
                UserDTO.class,
                id
        );

        return responseEntity.getStatusCode() == HttpStatus.OK
                ? Optional.ofNullable(responseEntity.getBody()) : Optional.empty();
    }

    @Override
    public Optional<UserDTO> create(UserDTO userDTO) {
        ResponseEntity<UserDTO> responseEntity = restTemplate.postForEntity(
                "http://localhost:8081/api/v1/user/create",
                userDTO,
                UserDTO.class
        );

        return responseEntity.getStatusCode() == HttpStatus.OK
                ? Optional.ofNullable(responseEntity.getBody()) : Optional.empty();
    }

    @Override
    public HttpStatus update(UserDTO userDTO) {
        ResponseEntity<?> responseEntity = restTemplate.postForEntity(
                "http://localhost:8081/api/v1/user/update",
                userDTO,
                UserDTO.class
        );

        return responseEntity.getStatusCode();
    }

    @Override
    public HttpStatus delete(Long id) {
        ResponseEntity<?> responseEntity = restTemplate.getForEntity(
                "http://localhost:8081/api/v1/user/delete/{id}",
                ResponseEntity.class,
                id
        );

        return responseEntity.getStatusCode();
    }
}