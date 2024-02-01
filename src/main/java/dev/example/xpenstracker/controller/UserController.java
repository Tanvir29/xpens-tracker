package dev.example.xpenstracker.controller;

import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping({"/user/", "/user"})
@Controller
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
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
    public String addUser(@Valid @ModelAttribute("userInfo") UserInfo userInfo){
        userService.postUserInfo(userInfo);
        return "redirect:/user";
    }

    @PostMapping("/updateUser")
    public String updateUser(@Valid @ModelAttribute("userInfo") UserInfo userInfo){
        userService.postUserInfo(userInfo);
        return "redirect:/user/" + userInfo.getId();
    }

    public UserInfo getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}")
    public String userHomePage(@PathVariable Long id,
                               Model model){
        UserInfo userInfo = getUserById(id);
        model.addAttribute("userInfo", userInfo);
        return "userPage";
    }
    @GetMapping("/{id}/update/")
    public String updateUserInfo(@PathVariable Long id,
                                      Model model) {
        UserInfo userInfo = getUserById(id);
        model.addAttribute("userInfo", userInfo);
        return "updateUser";
    }

}
