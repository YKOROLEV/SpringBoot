package ru.korolev.springboot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.korolev.springboot.model.User;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserDTO {

    private Long id;
    @NotNull
    private String login;
    @NotNull
    private String name;
    private String password;
    private Collection<RoleDTO> roles;

    public UserDTO() {
    }

    public UserDTO(String login, String name, String password) {
        this.login = login;
        this.name = name;
        this.password = password;
    }

    public UserDTO(Long id, String login, String name, String password) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.password = password;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.name = user.getName();
        this.password = user.getPassword();

        this.roles = user.getRoles()
                .stream()
                .map(RoleDTO::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<RoleDTO> getRoles() {
        return roles;
    }

    @JsonIgnore
    public String getRolesString() {
        return this.roles
                .stream()
                .map(RoleDTO::getName)
                .collect(Collectors.joining(", "));
    }

    public void setRoles(Collection<RoleDTO> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public boolean hasAdmin() {
        return this.roles != null && this.roles
                .stream()
                .anyMatch(role -> "ADMIN".contains(role.getName()));
    }

    @JsonIgnore
    public boolean hasUser() {
        return this.roles != null && this.roles
                .stream()
                .allMatch(role -> "USER".contains(role.getName()));
    }

    @JsonIgnore
    public boolean hasRoles() {
        return this.roles != null;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}