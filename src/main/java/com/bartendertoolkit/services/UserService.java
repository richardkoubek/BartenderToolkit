package com.bartendertoolkit.services;

import com.bartendertoolkit.models.User;
import jakarta.servlet.http.Cookie;

import java.util.Optional;

public interface UserService {
    User createNewUser(String email, String password, String userName);
    void setTokenToUser(User user, String token);
    void removeToken(UserDetailsImpl userDetails);
    Optional<User> findById(Long userId);
    Optional<User> findUserByUsernameAndPassword(String userName, String password);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
    boolean isCorrectEmailFormat(String email);
    boolean isCorrectPasswordFormat(String password);
    boolean isCorrectPasswordFormat(User user, String password);
    void validateNewUser(String email, String password) throws Exception;
    void checkIfExistsByEmailAndUserName(String email, String userName) throws Exception;
    void checkCredentials(String email) throws Exception;
}
