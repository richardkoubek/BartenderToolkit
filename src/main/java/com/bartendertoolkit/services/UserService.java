package com.bartendertoolkit.services;

import com.bartendertoolkit.models.User;

import java.util.Optional;

public interface UserService {
    void createNewUser(String email, String password, String userName);
    Optional<User> findById(Long userId);
    Optional<User> findUserByUsernameAndPassword(String userName, String password);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean isCorrectEmailFormat(String email);
    boolean isCorrectPasswordFormat(String password);
    boolean isValidPassword(User user, String password);
    void validateNewUser(String email, String password) throws Exception;
    String getUserToken(String password);
}
