package rajib.springframework.jpaexamples.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rajib.springframework.jpaexamples.entities.User;
import rajib.springframework.jpaexamples.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String name, String email) {
        User user = new User(name, email);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, String newName, String newEmail) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(newName);
                    user.setEmail(newEmail);
                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

