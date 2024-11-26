package pk.ztp.filmbase.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pk.ztp.filmbase.model.User;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_FindByUsername_ReturnsUser() {
        // Arrange
        User user = new User("test_username", "test_password");
        userRepository.save(user);
        // Act
        Optional<User> userOptional = userRepository.findByUsername("test_username");
        // Assert
        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertEquals("test_username", userOptional.get().getUsername());
        Assertions.assertEquals("test_password", userOptional.get().getPassword());
    }

    @Test
    public void UserRepository_FindByUsername_ReturnsEmptyOptional() {
        // Act
        Optional<User> userOptional = userRepository.findByUsername("test_username");
        //Assert
        Assertions.assertFalse(userOptional.isPresent());
    }
}
