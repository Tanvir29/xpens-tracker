package dev.example.xpenstracker.controller;

import dev.example.xpenstracker.model.UserInfo;
import dev.example.xpenstracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomePageController {
    private final UserService userService;

    public HomePageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String homePage(){
        return "logIn";
    }
    @GetMapping("/admin/")
    public String adminPage(){
        return "adminPage";
    }
    @PostMapping
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        if (email.equalsIgnoreCase("admin@gmail.com") &&
            password.equalsIgnoreCase("admin")){
            return "redirect:/admin/";
        }
        UserInfo user = userService.getUserfromLoginInfo(email,password);
        if (user != null) {
            model.addAttribute("message", "Login successful!");
            return "redirect:/user/" + user.getId();
        } else {
            model.addAttribute("error", "Invalid email or password. Please try again.");
            return "logIn";
        }
    }

}
