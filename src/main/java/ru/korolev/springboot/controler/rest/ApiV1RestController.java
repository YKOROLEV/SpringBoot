package ru.korolev.springboot.controler.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.korolev.springboot.model.dto.UserDTO;
import ru.korolev.springboot.service.RestService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class ApiV1RestController {

    private final RestService restService;

    public ApiV1RestController(RestService restService) {
        this.restService = restService;
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserDTO>> users() {
        List<UserDTO> userDTOList = restService.getAll();
        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> user(@PathVariable Long id) {
        Optional<UserDTO> userDTO = restService.getById(id);
        return userDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/user/create")
    public ResponseEntity<UserDTO> userCreate(@RequestBody UserDTO userDTO) {
        Optional<UserDTO> newUser = restService.create(userDTO);
        return newUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/user/update")
    public ResponseEntity<?> userUpdate(@RequestBody UserDTO userDTO) {
        HttpStatus status = restService.update(userDTO);
        return ResponseEntity.status(status).build();
    }

    @GetMapping("/user/delete/{id}")
    public ResponseEntity<?> userDelete(@PathVariable Long id, Principal principal) {
        HttpStatus status = restService.delete(id);
        return ResponseEntity.status(status).build();
    }
}