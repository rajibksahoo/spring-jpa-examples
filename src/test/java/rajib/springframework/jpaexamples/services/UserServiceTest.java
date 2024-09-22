package rajib.springframework.jpaexamples.services;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rajib.springframework.jpaexamples.entities.User;
import rajib.springframework.jpaexamples.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        // Arrange
        User user = new User("John Doe", "john.doe@example.com");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser("John Doe", "john.doe@example.com");

        // Assert
        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john.doe@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(new User("User1", "user1@example.com"), new User("User2", "user2@example.com"));
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).getName());
        assertEquals("User2", result.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById_ExistingId() {
        // Arrange
        User user = new User("User1", "user1@example.com");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("User1", result.get().getName());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetUserById_NonExistingId() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testUpdateUser_ExistingId() {
        // Arrange
        User existingUser = new User("Old Name", "old.email@example.com");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        User updatedUser = userService.updateUser(1L, "New Name", "new.email@example.com");

        // Assert
        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName());
        assertEquals("new.email@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser_NonExistingId() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, "New Name", "new.email@example.com"));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testDeleteUser_ExistingId() {
        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteUser_NonExistingId() {
        // Act
        userService.deleteUser(99L);

        // Assert
        verify(userRepository, times(1)).deleteById(anyLong());
    }
}
