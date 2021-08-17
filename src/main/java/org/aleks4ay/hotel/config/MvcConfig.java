package org.aleks4ay.hotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
//        registry.addViewController("/").setViewName("redirect:shop");
//        registry.addViewController("/home").setViewName("redirect:shop");
//        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/shop").setViewName("shop");
    }

}