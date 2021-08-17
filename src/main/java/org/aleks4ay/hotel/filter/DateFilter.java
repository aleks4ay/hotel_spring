package org.aleks4ay.hotel.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//@WebFilter( urlPatterns = { "/user" })
public class DateFilter /*implements Filter*/ {
    /*@Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpSession session = ((HttpServletRequest) request).getSession();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String stringArrival = request.getParameter("arrival");
        String stringDeparture = request.getParameter("departure");

        LocalDate dateStart;
        LocalDate dateEnd;

        if (stringArrival != null && !stringArrival.isEmpty()) {
            dateStart = LocalDate.parse(stringArrival, formatter);
            request.setAttribute("arrival", dateStart);
            session.setAttribute("arrival", dateStart);
        } else {
            request.setAttribute("arrival", session.getAttribute("arrival"));
        }
        if (stringDeparture != null && !stringDeparture.isEmpty()) {
            dateEnd = LocalDate.parse(stringDeparture, formatter);
            request.setAttribute("departure", dateEnd);
            session.setAttribute("departure", dateEnd);
        } else {
            request.setAttribute("departure", session.getAttribute("departure"));
        }
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }*/
}
