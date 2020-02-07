package ru.korolev.springboot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.korolev.springboot.dao.RoleDAO;
import ru.korolev.springboot.dao.UserDAO;
import ru.korolev.springboot.model.Role;
import ru.korolev.springboot.model.User;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    public RoleServiceImpl(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @Override
    public void setRoleAdmin(User user) {
        if (user.getRoles() != null) {
            user.getRoles().clear();
        }

        user.setRoles(roleAdmin());
        userDAO.save(user);
    }

    @Override
    public void setRoleUser(User user) {
        if (user.getRoles() != null) {
            user.getRoles().clear();
        }

        user.setRoles(roleUser());
        userDAO.save(user);
    }


    private Collection<Role> roleAdmin() {
        Collection<Role> roles = new ArrayList<>();
        roles.add(getRoleAdmin());
        roles.add(getRoleUser());
        return roles;
    }

    private Collection<Role> roleUser() {
        Collection<Role> roles = new ArrayList<>();
        roles.add(getRoleUser());
        return roles;
    }

    private Role getRoleAdmin() {
        return roleDAO.findByName("ADMIN").orElse(null);
    }

    private Role getRoleUser() {
        return roleDAO.findByName("USER").orElse(null);
    }
}