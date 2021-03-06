package ru.korolev.springboot.service;

import ru.korolev.springboot.model.User;
import ru.korolev.springboot.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<User> findByLogin(String login);

    List<User> findAll();

    void update(User user);

    void save(User user);

    void delete(Long id);

    void delete(User user);

    Optional<UserDTO> getById(Long id);

    Optional<UserDTO> getByLogin(String login);

    List<UserDTO> getAll();

    void update(UserDTO userDTO);
}