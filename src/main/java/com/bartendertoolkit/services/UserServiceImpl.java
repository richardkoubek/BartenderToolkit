package com.bartendertoolkit.services;

import com.bartendertoolkit.models.User;
import com.bartendertoolkit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Pbkdf2PasswordEncoder passwordEncoder;
    private static final String EMAIL_REGEX =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_-])[A-Za-z\\d@$!%*?&_-]{8,}$";

    @Override
    public User createNewUser(String email, String password, String userName) {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }

    @Override
    public void setTokenToUser(User user, String token){
        user.setUserToken(token);
        userRepository.save(user);
    }

    @Override
    public void removeToken(UserDetailsImpl userDetails){
        User user = userRepository.findByEmail(userDetails.getEmail());
        user.setUserToken(null);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findUserByUsernameAndPassword(String userName, String password) {
        if(!StringUtils.hasText(userName) && StringUtils.hasText(password)){
            throw new RuntimeException("Username or password is empty");
        }
        var userEnt = userRepository.findUserByUserNameAndPassword(userName, password);
        if (userEnt.isEmpty()){
            throw new RuntimeException("Username or password incorrect");
        }

        return userEnt;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUserName(String userName){
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean isCorrectEmailFormat(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email.trim());
        return matcher.matches();
    }

    @Override
    public boolean isCorrectPasswordFormat(String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @Override
    public boolean isCorrectPasswordFormat(User user, String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @Override
    public void validateNewUser(String email, String password) throws Exception {
        if (!isCorrectEmailFormat(email)) {
            throw new Exception("Invalid E-mail address.");
        }
        if (!isCorrectPasswordFormat(password)) {
            throw new Exception("Invalid Password.");
        }
    }

    @Override
    public void checkIfExistsByEmailAndUserName(String email, String userName) throws Exception {
        if (existsByEmail(email)) {
            throw new Exception("User with this email already exists.");
        }
        if(existsByUserName(userName)){
            throw new Exception("User with this user name already exists.");
        }
    }

    @Override
    public void checkCredentials(String email) throws Exception {
        if (!existsByEmail(email)){
            throw new Exception("User with this email doesn't exists.");
        }
    }
}
