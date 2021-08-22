package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

class Utils {

    static void parseDate(Map<String, Object> model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String stringArrival = request.getParameter("arrival");
        String stringDeparture = request.getParameter("departure");

        LocalDate dateStart;
        LocalDate dateEnd;

        if (stringArrival != null && !stringArrival.isEmpty()) {
            dateStart = LocalDate.parse(stringArrival, formatter);
            model.put("arrival", dateStart);
            session.setAttribute("arrival", dateStart);
        } else {
            model.put("arrival", session.getAttribute("arrival"));
        }
        if (stringDeparture != null && !stringDeparture.isEmpty()) {
            dateEnd = LocalDate.parse(stringDeparture, formatter);
            model.put("departure", dateEnd);
            session.setAttribute("departure", dateEnd);
        } else {
            model.put("departure", session.getAttribute("departure"));
        }
    }

    static int initPageAttributes(Map<String, Object> model, HttpServletRequest request, int positions) {
        int page = 1;
        if (request.getSession().getAttribute("sortMethod") == null) {
            request.getSession().setAttribute("sortMethod", "byRoomNumber");
        }
        model.put("sortMethod", request.getSession().getAttribute("sortMethod"));

        if (request.getParameter("pg") != null) {
            page = Integer.parseInt(request.getParameter("pg"));
        }
        model.put("itemOnPage", positions);
        model.put("pg", page);

        return page;
    }

    static void doFiltering(Map<String, Object> model, HttpServletRequest request) {

        String filterButtonName = request.getParameter("filter");

        if (filterButtonName != null && filterButtonName.equalsIgnoreCase("filterCansel")) {
            request.removeAttribute("category");
            request.removeAttribute("guests");
            model.remove("category");
            model.remove("guests");
            request.getSession().removeAttribute("category");
            request.getSession().removeAttribute("guests");
            request.getSession().removeAttribute("arrival");
            request.getSession().removeAttribute("departure");
        } else {
            if (!request.getParameter("filter_category").equalsIgnoreCase("Select Category")) {
                Category category = Category.valueOf(request.getParameter("filter_category"));
                request.getSession().setAttribute("category", category);
            }
            if (!request.getParameter("filter_guests").equals("0")) {
                request.getSession().setAttribute("guests", Integer.parseInt(request.getParameter("filter_guests")));
            }
        }
    }
}
