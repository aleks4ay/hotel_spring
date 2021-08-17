package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class LanguageController {

    @GetMapping("/lang")
    public String execute(HttpServletRequest request, HttpSession session, @RequestParam(defaultValue = "ru") String language) {

        session.setAttribute("language", language);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                String path = new URL(request.getHeader("Referer")).getFile();
                return "redirect:" + path;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return "index";
    }
}
