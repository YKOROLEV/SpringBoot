package ru.korolev.springboot.service;

import org.springframework.stereotype.Service;
import ru.korolev.springboot.model.Role;
import ru.korolev.springboot.model.User;
import ru.korolev.springboot.repository.RoleRepository;
import ru.korolev.springboot.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class RoleServiceImpl implements RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RoleServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void setRoleAdmin(User user) {
        if (user.getRoles() != null) {
            user.getRoles().clear();
        }

        user.setRoles(roleAdmin());
        userRepository.save(user);
    }

    @Override
    public void setRoleUser(User user) {
        if (user.getRoles() != null) {
            user.getRoles().clear();
        }

        user.setRoles(roleUser());
        userRepository.save(user);
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
        return roleRepository.findByName("ADMIN").orElse(null);
    }

    private Role getRoleUser() {
        return roleRepository.findByName("USER").orElse(null);
    }
}