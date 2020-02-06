package ru.korolev.springboot.dao;

import java.util.List;
import java.util.Optional;

public interface AbstractDTO<T> {

    Optional<T> getById(Long id);

    Optional<T> getByLogin(String login);

    List<T> getAll();

    void update(T entity);
}