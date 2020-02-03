package ru.korolev.springboot.controler.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.korolev.springboot.model.User;
import ru.korolev.springboot.model.dto.UserDTO;
import ru.korolev.springboot.service.RoleService;
import ru.korolev.springboot.service.UserDtoService;
import ru.korolev.springboot.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class ApiV1RestController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserDtoService userDtoService;

    public ApiV1RestController(UserService userService, RoleService roleService, UserDtoService userDtoService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userDtoService = userDtoService;
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserDTO>> users() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(userDtoService.toDto(users));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> user(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);

        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        UserDTO userDTO = userDtoService.toDto(user.get());
        System.out.println(userDTO.getRolesString());
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/user/create")
    public ResponseEntity<UserDTO> userCreate(@RequestBody UserDTO userDTO) {
        User newUser = new User(userDTO);

        try {
            userService.save(newUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (!userDTO.hasRoles()) {
            roleService.setRoleUser(newUser);
        } else if (userDTO.hasAdmin()) {
            roleService.setRoleAdmin(newUser);
        } else if (userDTO.hasUser()) {
            roleService.setRoleUser(newUser);
        }

        return ResponseEntity.ok(userDtoService.toDto(newUser));
    }

    @PostMapping("/user/update")
    public ResponseEntity<?> userUpdate(@RequestBody UserDTO userDTO) {
        User currentUser = userDtoService.toEntity(userDTO);

        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        if (!userDTO.hasRoles()) {
            roleService.setRoleUser(currentUser);
        } else if (userDTO.hasAdmin()) {
            roleService.setRoleAdmin(currentUser);
        } else if (userDTO.hasUser()) {
            roleService.setRoleUser(currentUser);
        }

        try {
            userService.save(currentUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/delete/{id}")
    public ResponseEntity<?> userDelete(@PathVariable Long id, Principal principal) {
        Optional<User> user = userService.findById(id);

        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (user.get().getLogin().equals(principal.getName())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            user.get().getRoles().clear();
            userService.delete(user.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}