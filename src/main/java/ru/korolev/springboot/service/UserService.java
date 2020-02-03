package ru.korolev.springboot.service;

import ru.korolev.springboot.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<User> findByLogin(String login);

    List<User> findAll();

    void save(User user);

    void delete(User user);
}