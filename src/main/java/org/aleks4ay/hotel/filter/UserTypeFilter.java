package org.aleks4ay.hotel.filter;

import org.aleks4ay.hotel.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter( urlPatterns = { "/*" })
public class UserTypeFilter /*implements Filter*/ {
    /*@Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpSession session = ((HttpServletRequest) servletRequest).getSession();
        Object userObject = session.getAttribute("user");

        String userType;
        if(userObject == null) {
            userType = "guest";
        } else {
            User user = (User) userObject;
            userType = user.getRole().getTitle().toLowerCase();
        }

        servletRequest.setAttribute("userType", userType);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }*/
}
