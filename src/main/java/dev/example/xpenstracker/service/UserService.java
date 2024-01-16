package dev.example.xpenstracker.service;

import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
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

    public UserInfo updateUserPhoneNo(Long userInfoId, String phoneNo) {
        UserInfo userRecord = userRepository.findById(userInfoId).get();
        userRecord.setPhoneNo(phoneNo);
        return userRepository.save(userRecord);
    }

    public UserInfo updateUserEmailId(Long userInfoId, String email) {
        UserInfo userRecord = userRepository.findById(userInfoId).get();
        userRecord.setEmail(email);
        return userRepository.save(userRecord);
    }
}
