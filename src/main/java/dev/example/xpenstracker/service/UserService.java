package dev.example.xpenstracker.service;

import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfo postUserInfo(UserInfo userInfo) {
        return userRepository.save(userInfo);
    }

    public List<UserInfo> getUserList() {
        return userRepository.findAll();
    }

    public UserInfo getUserById(Long userInfoId) {
        return userRepository.findById(userInfoId).get();
    }

    public UserInfo getUserfromLoginInfo(String email, String password){
        return userRepository.findByEmailAndPassword(email, password);
    }
}
