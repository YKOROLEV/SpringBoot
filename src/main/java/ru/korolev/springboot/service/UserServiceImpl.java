package ru.korolev.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.korolev.springboot.dao.UserDAO;
import ru.korolev.springboot.model.User;
import ru.korolev.springboot.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userDAO.findByLogin(login);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public void delete(Long id) {
        userDAO.delete(id);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public Optional<UserDTO> getById(Long id) {
        return userDAO.getById(id);
    }

    @Override
    public Optional<UserDTO> getByLogin(String login) {
        return userDAO.getByLogin(login);
    }

    @Override
    public List<UserDTO> getAll() {
        return userDAO.getAll();
    }

    @Override
    public void update(UserDTO userDTO) {
        userDAO.update(userDTO);
    }
}