package ru.korolev.springboot.dao;

import ru.korolev.springboot.model.User;
import ru.korolev.springboot.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends AbstractDTO<UserDTO> {

    Optional<User> findById(Long id);

    Optional<User> findByLogin(String login);

    List<User> findAll();

    void update(User user);

    void save(User user);

    void delete(Long id);

    void delete(User user);
}