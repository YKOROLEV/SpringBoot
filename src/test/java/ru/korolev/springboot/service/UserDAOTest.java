package ru.korolev.springboot.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestPropertySource;
import ru.korolev.springboot.dao.UserDAO;
import ru.korolev.springboot.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.datasource.initialization-mode=always",
        "spring.jpa.show-sql=false",
        "spring.datasource.data=classpath:data_test.sql"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @Order(0)
    public void testInitialData() {
        assertEquals("admin", userDAO.findById(1L).map(User::getLogin).orElse(null));
        assertEquals("user", userDAO.findById(2L).map(User::getLogin).orElse(null));
        assertNull(userDAO.findById(3L).map(User::getLogin).orElse(null));
    }

    @Test
    @Order(1)
    public void testFindByIdExisting() {
        User user = userDAO.findById(1L).orElse(null);
        assertNotNull(user);
    }

    @Test
    @Order(2)
    public void testFindByIdNotExisting() {
        User user = userDAO.findById(3L).orElse(null);
        assertNull(user);
    }

    @Test
    @Order(3)
    public void testFindByLoginExisting() {
        User user = userDAO.findByLogin("admin").orElse(null);
        assertNotNull(user);
    }

    @Test
    @Order(4)
    public void testFindByLoginNotExisting() {
        User user = userDAO.findByLogin("fake").orElse(null);
        assertNull(user);
    }

    @Test
    @Order(5)
    public void testFindAll() {
        List<User> userList = userDAO.findAll();
        assertEquals(2, userList.size());
        assertEquals("admin", userList.get(0).getLogin());
        assertEquals("user", userList.get(1).getLogin());
    }

    @Test
    @Order(6)
    public void testUpdateExisting() {
        User user = userDAO.findById(1L)
                .orElseThrow(RuntimeException::new);

        user.setLogin("testUpdateLogin");
        user.setName("testUpdateName");
        user.setPassword("testUpdatePassword");

        userDAO.update(user);

        user = userDAO.findById(1L)
                .orElseThrow(RuntimeException::new);

        assertEquals("testUpdateLogin", user.getLogin());
        assertEquals("testUpdateName", user.getName());
        assertEquals("testUpdatePassword", user.getPassword());
    }

    @Test
    @Order(7)
    public void testUpdateExistingAndDuplicateLogin() {
        User user = userDAO.findById(1L)
                .orElseThrow(RuntimeException::new);

        user.setLogin("user");

        assertThrows(
                DataIntegrityViolationException.class,
                () -> userDAO.update(user)
        );
    }


    @Test
    @Order(8)
    public void testUpdateNotExisting() {
        User user = null;

        Throwable throwable = assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> userDAO.update(user)
        );

        assertTrue(throwable.getMessage().contains("attempt to create merge event with null entity"));
        assertEquals(IllegalArgumentException.class, throwable.getCause().getClass());
    }
}