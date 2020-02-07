package ru.korolev.springboot.dao;

import ru.korolev.springboot.model.Role;

import java.util.Optional;

public interface RoleDAO {

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);

    void update(Role role);

    void save(Role role);
}