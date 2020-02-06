package ru.korolev.springboot.model;

import ru.korolev.springboot.model.dto.UserDTO;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user")
@SqlResultSetMapping(
        name = "UserDTOMapping",
        classes = @ConstructorResult(
                targetClass = UserDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "login"),
                        @ColumnResult(name = "name"),
                        @ColumnResult(name = "password")
                }
        )
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;

    @Column
    private String name;
    @Column
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @Column
    private Collection<Role> roles;

    public User() {

    }

    public User(String login, String name, String password) {
        this.login = login;
        this.name = name;
        this.password = password;
    }

    public User(Long id, String login, String name, String password) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.password = password;
    }

    public User(String login, String name, String password, Collection<Role> roles) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public User(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.login = userDTO.getLogin();
        this.name = userDTO.getName();
        this.password = userDTO.getPassword();
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}