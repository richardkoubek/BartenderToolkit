package com.bartendertoolkit.services;

import com.bartendertoolkit.models.User;
import com.bartendertoolkit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
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
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
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
