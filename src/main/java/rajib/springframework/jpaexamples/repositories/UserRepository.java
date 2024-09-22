package rajib.springframework.jpaexamples.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rajib.springframework.jpaexamples.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
