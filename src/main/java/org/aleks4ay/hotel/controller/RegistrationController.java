package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.exception.AlreadyException;
import org.aleks4ay.hotel.model.Role;
import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    private static final Logger log = LogManager.getLogger(RegistrationController.class);

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Map<String, Object> model) {
        model.put("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String newUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Map<String, Object> model) {

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors() ) {
                model.put("wrongLogin", error.getDefaultMessage());
            }
            return "registration";
        }
        try {
            user.setActive(true);
            user.addRole(Role.ROLE_USER);
            userService.save(user);
            log.info("Was registered new User '{}'", user.getLogin());
        } catch (AlreadyException e) {
            model.put("wrongLogin", "User with login " + user.getLogin() + " already exists!");
            return "registration";
        }
        return "redirect:/login";
    }
}
