package com.bartendertoolkit.services;

import com.bartendertoolkit.models.User;

import java.util.Optional;

public interface UserService {
    Optional<User> createNewUser(String email, String userName, String password);
    Optional<User> findById(Long userId);
    Optional<User> findUserByUsernameAndPassword(String userName, String password);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean isCorrectEmailFormat(String email);
    boolean isValidPassword(User user, String password);
}
