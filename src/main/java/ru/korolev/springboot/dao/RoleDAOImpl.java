package ru.korolev.springboot.dao;

import org.springframework.stereotype.Repository;
import ru.korolev.springboot.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class RoleDAOImpl implements RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Role> findById(Long id) {
        try {
            TypedQuery<Role> query = entityManager.createQuery("from Role where id = :id", Role.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Role> findByName(String name) {
        try {
            TypedQuery<Role> query = entityManager.createQuery("from Role where name = :name", Role.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Role role) {
        entityManager.merge(role);
    }

    @Override
    public void save(Role role) {
        entityManager.persist(role);
    }
}