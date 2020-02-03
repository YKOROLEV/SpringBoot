package ru.korolev.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.korolev.springboot.model.Role;
import ru.korolev.springboot.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Login " + login + " not found."));

        org.springframework.security.core.userdetails.User.UserBuilder builder =
                org.springframework.security.core.userdetails.User.withUsername(login);
        builder.password("{noop}" + user.getPassword());
        builder.roles(getUserRoles(user));

        return builder.build();
    }

    private String[] getUserRoles(User user) {
        return user.getRoles()
                    .stream()
                    .map(Role::getName)
                    .toArray(String[]::new);
    }
}