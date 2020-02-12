package ru.korolev.springboot.service;

import org.springframework.http.HttpStatus;
import ru.korolev.springboot.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface RestService {

    Optional<UserDTO> authorization(String login, String password);

    Optional<UserDTO> authorization(boolean social, String login, String name);

    List<UserDTO> getAll();

    Optional<UserDTO> getById(Long id);

    Optional<UserDTO> create(UserDTO userDTO);

    HttpStatus update(UserDTO userDTO);

    HttpStatus delete(Long id);

    void setBasicAuthentication(String login, String password);
}