package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class LoginController {
    private static final Logger log = LogManager.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String newLogin(HttpServletRequest request) {
        log.info("Try new exit");
        addBackUrl(request);
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String execute(/*@ModelAttribute User user,*/
            HttpServletRequest request, Model model,
                          @RequestParam String login,
                          @RequestParam String password) {
        System.out.println("login = " + login);
        System.out.println("pass = " + password);

        if (!userService.checkLogin(login)) {
            log.info("Login '{}' is wrong!", login);
            model.addAttribute("wrongLogin", "Login is wrong!");
            model.addAttribute("oldLogin", login);
            return "login";
        }

        if (!userService.checkPassword(login, password)) {
            log.info("Login '{}' or password is wrong!", login);
            model.addAttribute("wrongPass", "Password is wrong!");
            model.addAttribute("oldLogin", login);
            return "login";
        } else {
            Optional<User> userOptional = userService.getByLogin(login);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                request.getSession().setAttribute("user", user);
                if (user.isAdmin()) {
                    model.addAttribute("action", "user");
                    log.info("Admin '{}' exited on site", login);
                    return "redirect:/admin";
                }
                if (user.isManager()) {
                    model.addAttribute("action", "order");
                    log.info("Manager '{}' exited on site", login);
                    return "redirect:/manager?action=order";
                }
                if (user.isClient()) {
                    model.addAttribute("action", "room");
                    log.info("User '{}' exited on site", login);
                    return "redirect:/user/room";
                }
            }
            return "index";
        }
    }

    private void addBackUrl(HttpServletRequest request) {
        try {
            String url = new URL(request.getHeader("Referer")).getFile();
            if (!url.contains("registration") && !url.contains("login") ) {
                request.getSession().setAttribute("backUrl", url);
            }
        } catch (MalformedURLException e) {
            log.warn("Can't read 'Referer' from request. {}", e);
        }
    }
}
