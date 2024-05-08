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
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    @Override
    public Optional<User> createNewUser(String email, String userName, String password) {
        if (!checkEmailFormat(email)){
         throw new RuntimeException("invalid email");
        }
        if(!StringUtils.hasText(userName) && StringUtils.hasText(password)){
            throw new RuntimeException("Username or password is empty");
        }
        if(userRepository.existsByUserName(userName)){
            return Optional.empty();
        }

        User userEnt =  new User(email, userName, password);

        userRepository.save(userEnt);
        return Optional.of(userEnt);
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
    public boolean isValidPassword(User user, String password) {
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

    public boolean checkEmailFormat(String email){
        if (!StringUtils.hasText(email)) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email.trim());
        return matcher.matches();
    }
}
