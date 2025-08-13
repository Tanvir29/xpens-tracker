package dev.example.xpenstracker.service;

import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserInfo postUserInfo(UserInfo userInfo) {
        if (userInfo.getPassword() != null && !isPasswordEncoded(userInfo.getPassword())) {
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        }
        return userRepository.save(userInfo);
    }

    public List<UserInfo> getUserList() {
        return userRepository.findAll();
    }

    public UserInfo getUserById(Long userInfoId) {
        return userRepository.findById(userInfoId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userInfoId));
    }

    public UserInfo getUserfromLoginInfo(String email, String password){
        UserInfo user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    private boolean isPasswordEncoded(String raw) {
        return raw != null && (raw.startsWith("$2a$") || raw.startsWith("$2b$") || raw.startsWith("$2y$"));
    }
}
