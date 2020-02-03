package ru.korolev.springboot.service;

import ru.korolev.springboot.model.User;

public interface RoleService {

    void setRoleAdmin(User user);

    void setRoleUser(User user);
}