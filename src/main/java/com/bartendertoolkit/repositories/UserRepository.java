package com.bartendertoolkit.repositories;

import com.bartendertoolkit.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<User> findUserByUserNameAndPassword(String userName, String password);
    User findByEmail(String email);
}
