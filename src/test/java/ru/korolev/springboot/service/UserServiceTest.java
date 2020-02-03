package ru.korolev.springboot.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import ru.korolev.springboot.model.User;
import ru.korolev.springboot.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.datasource.initialization-mode=always",
        "spring.jpa.show-sql=false",
        "spring.datasource.data=classpath:data_test.sql"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    private static final Long FAKE_ID = 99L;
    private static final String FAKE_LOGIN = "FAKE";

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @Order(0)
    public void testInitialData() {
        assertEquals("admin", userService.findById(1L).map(User::getLogin).orElse(null));
        assertEquals("user", userService.findById(2L).map(User::getLogin).orElse(null));
        assertNull(userService.findById(3L).map(User::getLogin).orElse(null));
    }

    @Test
    @Order(1)
    public void findById_Found() {
        User user = userService.findById(1L)
                .orElseThrow(RuntimeException::new);

        assertEquals(1L, user.getId());
        assertEquals("admin", user.getLogin());
    }

    @Test
    @Order(2)
    public void findById_NotFound() {
        assertEquals(Optional.<User>empty(), userService.findById(FAKE_ID));
    }

    @Test
    @Order(3)
    public void findByLogin_Found() {
        User user = userService.findByLogin("admin")
                .orElseThrow(RuntimeException::new);

        assertEquals(1L, user.getId());
        assertEquals("admin", user.getLogin());
    }

    @Test
    @Order(4)
    public void findByLogin_NotFound() {
        assertEquals(Optional.<User>empty(), userService.findByLogin(FAKE_LOGIN));
    }

    @Test
    @Order(5)
    public void findAll() {
        assertEquals(2, userService.findAll().size());
    }

    @Test
    @Order(6)
    public void save_Success() {
        User newUser = new User("login", "name", "pass");
        userService.save(newUser);

        assertTrue(newUser.getId() > 0);
        assertEquals("login", newUser.getLogin());
        assertEquals("name", newUser.getName());
        assertEquals("pass", newUser.getPassword());

        int storageSize = userService.findAll().size();
        assertEquals(3, storageSize);
    }

    @Test
    @Order(7)
    public void save_ErrorDuplicate() {
        User duplicateUser = new User("admin", "tomato", "123");

        assertThrows(
                DataIntegrityViolationException.class,
                () -> userService.save(duplicateUser)
        );
    }
}