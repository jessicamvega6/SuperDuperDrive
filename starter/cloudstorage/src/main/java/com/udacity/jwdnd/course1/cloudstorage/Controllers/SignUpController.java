package com.udacity.jwdnd.course1.cloudstorage.Controllers;

import com.udacity.jwdnd.course1.cloudstorage.Entity.User;
import com.udacity.jwdnd.course1.cloudstorage.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignUpController {

    private UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signUp(@ModelAttribute("user") User user) {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
        boolean usernameAvailable = userService.isUsernameAvailable(user.getUsername());

        if(!usernameAvailable) {
            model.addAttribute("errorsMsg", "Username is already taken");
            return "signup";
        }

        int userCreated = userService.createUser(user);
        if(userCreated > 0) {
            model.addAttribute("successMsg", "You have successfully created a new user");
            user.setUsername("");
            user.setPassword("");
            user.setFirstname("");
            user.setLastname("");
            model.addAttribute("user", user);
        } else {
            model.addAttribute("errorsMsg", "Failed to create a new user");
        }
        redirectAttributes.addFlashAttribute("successMsg", "You successfully signed up!");

        return "redirect:/login";
    }





}
