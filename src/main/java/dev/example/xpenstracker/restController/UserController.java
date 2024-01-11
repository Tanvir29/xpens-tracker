package dev.example.xpenstracker.restController;

import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping({"/user/", "/user"})
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserInfo postUserInfo(@Valid @RequestBody UserInfo userInfo) {
        return userService.postUserInfo(userInfo);
    }

    @GetMapping
    public List<UserInfo> getUserList() {
        return userService.getUserList();
    }

    @GetMapping({"/{userId}/", "/{userId}"})
    public UserInfo getUserById(@PathVariable("userId") Long userInfoId) {
        return userService.getUserById(userInfoId);
    }

    @PutMapping({"/{userId}/updatePhone/", "/{userId}/updatePhone"})
    public UserInfo updateUserPhoneNo(@PathVariable("userId") Long userInfoId,
                                      @RequestBody String phoneNo) {
        return userService.updateUserPhoneNo(userInfoId, phoneNo);
    }

    @PutMapping({"/{userId}/updateMail", "/{userId}/updateMail/"})
    public UserInfo updateUserEmailId(@PathVariable("userId") Long userInfoId,
                                      @RequestBody String emailId) {
        return userService.updateUserEmailId(userInfoId, emailId);
    }

}
