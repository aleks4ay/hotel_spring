package org.aleks4ay.hotel.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//@WebFilter( urlPatterns = { "/user", "/admin" })
public class PaginationFilter /*implements Filter*/ {
    /*@Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        int page = 1;
        String stringPage = servletRequest.getParameter("pg");
        if (stringPage != null) {
            page = Integer.parseInt(stringPage);
        }
        servletRequest.setAttribute("pg", page);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }*/
}
