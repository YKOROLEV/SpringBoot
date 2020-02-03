package ru.korolev.springboot.service;

import org.springframework.stereotype.Service;
import ru.korolev.springboot.model.User;
import ru.korolev.springboot.model.dto.UserDTO;
import ru.korolev.springboot.repository.UserRepository;

@Service
public class UserDtoService implements DtoService<User, UserDTO> {

    private final UserRepository userRepository;

    public UserDtoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO toDto(User user) {
        return user == null ? null : new UserDTO(user);
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = null;

        if (userDTO.getId() != null) {
            user = userRepository.findById(userDTO.getId())
                    .orElse(null);
        }
        
        return user;
    }
}