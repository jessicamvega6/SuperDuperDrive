package com.udacity.jwdnd.course1.cloudstorage.Controllers;

import com.udacity.jwdnd.course1.cloudstorage.CommonComponents;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    CommonComponents commonComponents;

    public HomeController(CommonComponents commonComponents) {
        this.commonComponents = commonComponents;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        commonComponents.addCommonAttributes(model, authentication);
        return "home";
    }

}
