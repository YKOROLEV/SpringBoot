package ru.korolev.springboot.model;

import ru.korolev.springboot.model.dto.RoleDTO;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@SqlResultSetMapping(
        name = "RoleDTOMapping",
        classes = @ConstructorResult(
                targetClass = RoleDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name"),
                }
        )
)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    public Role() {

    }

    public Role(String name) {
    }

    public Role(RoleDTO roleDTO) {
        this.id = roleDTO.getId();
        this.name = roleDTO.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}