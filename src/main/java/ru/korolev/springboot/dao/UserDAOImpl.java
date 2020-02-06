package ru.korolev.springboot.dao;

import org.springframework.stereotype.Repository;
import ru.korolev.springboot.model.User;
import ru.korolev.springboot.model.dto.RoleDTO;
import ru.korolev.springboot.model.dto.UserDTO;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findById(Long id) {
        try {
            TypedQuery<User> query = entityManager.createQuery("from User where id = :id", User.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try {
            TypedQuery<User> query = entityManager.createQuery("from User where login = :login", User.class);
            query.setParameter("login", login);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        try {
            TypedQuery<User> query = entityManager.createQuery("from User", User.class);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public void delete(Long id) {
        User user = findById(id)
                .orElseThrow(IllegalArgumentException::new);
        entityManager.remove(user);
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Override
    public Optional<UserDTO> getById(Long id) {
        try {
            Query query = entityManager.createNativeQuery("select * from User where id = :id", "UserDTOMapping");
            query.setParameter("id", id);

            UserDTO userDTO = (UserDTO) query.getSingleResult();
            userDTO.setRoles(getRoles(id));

            return Optional.of(userDTO);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDTO> getByLogin(String login) {
        try {
            Query query = entityManager.createNativeQuery("select * from User where login = :login", "UserDTOMapping");
            query.setParameter("login", login);

            UserDTO userDTO = (UserDTO) query.getSingleResult();
            userDTO.setRoles(getRoles(userDTO.getId()));

            return Optional.of(userDTO);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserDTO> getAll() {
        try {
            Query query = entityManager.createNativeQuery("select * from User", "UserDTOMapping");
            List<UserDTO> userDTOList = (List<UserDTO>) query.getResultList();
            userDTOList.forEach(user -> user.setRoles(getRoles(user.getId())));
            return userDTOList;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void update(UserDTO userDTO) {
        try {
            Query query = entityManager.createNativeQuery("update User set login = :login, name = :name, password = :password where id = :id");
            query.setParameter("id", userDTO.getId());
            query.setParameter("login", userDTO.getLogin());
            query.setParameter("name", userDTO.getName());
            query.setParameter("password", userDTO.getPassword());

        } catch (PersistenceException ignored) {
        }
    }

    private List<RoleDTO> getRoles(Long id) {
        try {
            Query query = entityManager.createNativeQuery("select id, name from roles inner join user_roles ur on roles.id = ur.role_id where ur.user_id = :user_id", "RoleDTOMapping");
            query.setParameter("user_id", id);
            return (List<RoleDTO>) query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}