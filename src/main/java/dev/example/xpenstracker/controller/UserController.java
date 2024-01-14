package dev.example.xpenstracker.controller;

import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping({"/user/", "/user"})
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserInfo postUserInfo(@Valid @RequestBody UserInfo userInfo) {
        return userService.postUserInfo(userInfo);
    }

    @GetMapping
    public String getUserList(Model model){
        List<UserInfo> userInfos = userService.getUserList();
        model.addAttribute("listUsers", userInfos);
        return "userList";
    }

    @GetMapping("/newUserForm")
    public String showNewUserForm(Model model){
        UserInfo userInfo = new UserInfo();
        model.addAttribute("userInfo", userInfo);
        return "newUser";
    }

    @PostMapping("/saveUser")
    public String addUser(@ModelAttribute("userInfo") UserInfo userInfo){
        userService.postUserInfo(userInfo);
        return "redirect:/user";
    }

    //@GetMapping({"/{userId}/", "/{userId}"})
    public UserInfo getUserById(@PathVariable("userId") Long userInfoId) {
        return userService.getUserById(userInfoId);
    }

    @GetMapping("/updateUser/{id}")
    public String updateUserInfo(@PathVariable("id") Long userInfoId,
                                      Model model) {
        UserInfo userInfo = getUserById(userInfoId);
        model.addAttribute("userInfo", userInfo);
        return "updateUser";
    }

}
