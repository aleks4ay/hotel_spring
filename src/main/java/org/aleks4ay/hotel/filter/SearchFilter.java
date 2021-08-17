package org.aleks4ay.hotel.filter;

import org.aleks4ay.hotel.model.Category;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//@WebFilter( urlPatterns = { "/user" })
public class SearchFilter /*implements Filter */{
    /*@Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpSession session = ((HttpServletRequest) request).getSession();

        String categoryString = request.getParameter("filter_category");
        String guestsString = request.getParameter("filter_guests");
        if (categoryString != null) {
            request.setAttribute("category", Category.valueOf(categoryString));
            session.setAttribute("category", Category.valueOf(categoryString));
        } else {
            request.setAttribute("category", session.getAttribute("category"));
        }
        if (guestsString != null) {
            request.setAttribute("guests", Integer.parseInt(guestsString));
            session.setAttribute("guests", Integer.parseInt(guestsString));
        } else {
            request.setAttribute("guests", session.getAttribute("guests"));
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }*/
}
