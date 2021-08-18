package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.Role;
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
    private static final Logger log = LogManager.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Map<String, Object> model ) {
        return "registration";
    }

    @PostMapping("/registration")
    public String newUser(@ModelAttribute User user, Map<String, Object> model) {
        String login = user.getLogin();
        String firstName = user.getName();
        String lastName = user.getSurname();
        user.setActive(true);
        user.addRole(Role.ROLE_USER);
        if (userService.checkLogin(user.getLogin())) {
            model.put("wrongLogin", "User exists!");
            model.put("oldLogin", login);
            model.put("oldFirstName", firstName);
            model.put("oldLastName", lastName);
            return "registration";
        }
        userService.create(user);
        log.info("Was registered new User '{}'", user.getLogin());
        return "redirect:/login";
    }
}
