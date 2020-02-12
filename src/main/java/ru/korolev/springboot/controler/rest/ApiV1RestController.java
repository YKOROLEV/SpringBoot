package ru.korolev.springboot.controler.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.korolev.springboot.model.User;
import ru.korolev.springboot.model.dto.UserDTO;
import ru.korolev.springboot.service.RoleService;
import ru.korolev.springboot.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class ApiV1RestController {

    private final UserService userService;
    private final RoleService roleService;

    public ApiV1RestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/authorization")
    public ResponseEntity<UserDTO> authorization(@RequestParam(name = "social") Boolean social,
                                                 @RequestParam(name = "login") String login,
                                                 @RequestParam(name = "password") String password,
                                                 @RequestParam(name = "name") String name) {
        Optional<UserDTO> userDTO = userService.getByLogin(login);

        if (userDTO.isPresent()) {
            if (!social && !userDTO.get().getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            if (social) {
                User user = new User(login, name, password);
                userService.save(user);
                roleService.setRoleUser(user);
                userDTO = Optional.of(new UserDTO(user));
            }
        }

        return userDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/all")
    public ResponseEntity<List<UserDTO>> users() {
        List<UserDTO> userDTOList = userService.getAll();
        return ResponseEntity.ok(userDTOList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> user(@PathVariable Long id) {
        Optional<UserDTO> userDTO = userService.getById(id);
        return userDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PreAuthorize("hasRole('ADMIN')")
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

        userDTO = new UserDTO(newUser);
        return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user/update")
    public ResponseEntity<?> userUpdate(@RequestBody UserDTO userDTO) {
        User currentUser = userService.findById(userDTO.getId())
                .orElse(null);

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
            currentUser.setLogin(userDTO.getLogin());
            currentUser.setName(userDTO.getName());
            if (userDTO.getPassword() != null && userDTO.getPassword().length() > 0) {
                currentUser.setPassword(userDTO.getPassword());
            }

            userService.save(currentUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/delete/{id}")
    public ResponseEntity<?> userDelete(@PathVariable Long id, Principal principal) {
        Optional<User> user = userService.findById(id);

        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
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