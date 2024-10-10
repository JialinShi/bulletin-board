package com.jialin.BulletinBoard.repository;

import com.jialin.BulletinBoard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * The UserRepository interface is a JPA repository that abstracts away the data-access logic from the service layer.
 * It extends JpaRepository, providing CRUD operations and pagination capabilities over the User entity without
 * requiring explicit implementation
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
