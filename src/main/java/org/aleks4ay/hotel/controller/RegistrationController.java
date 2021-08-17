package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class RegistrationController {
    private static final Logger log = LogManager.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Map<String, Object> model ) {
        return "registration";
    }

    @PostMapping("/registration")
    public String newClient(@ModelAttribute User user) {
        user.setActive(true);
        user.setRole(User.Role.ROLE_USER);
        userService.create(user);
        log.info("Was registered new User '{}'", user.getLogin());
        return "redirect:/login";
    }
}
