package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

class Utils {

    private static void parseDate(Map<String, Object> model, HttpServletRequest request) {
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
        model.put("sortMethod", request.getSession().getAttribute("sortMethod") == null ? "id"
                : request.getSession().getAttribute("sortMethod"));
        request.getSession().setAttribute("sortMethod", model.get("sortMethod"));
        model.put("itemOnPage", positions);
        model.put("pg", request.getParameter("pg") == null ? 1 : Integer.parseInt(request.getParameter("pg")));
        return (int) model.get("pg");
    }

    static void doFiltering(Map<String, Object> model, HttpServletRequest request) {
        parseDate(model, request);

        String filterButtonName = request.getParameter("filter");

        if (filterButtonName != null && filterButtonName.equalsIgnoreCase("filterCansel")) {
            request.removeAttribute("category");
            request.removeAttribute("guests");
            request.removeAttribute("arrival");
            request.removeAttribute("departure");
            model.remove("category");
            model.remove("guests");
            model.remove("arrival");
            model.remove("departure");
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

    static void setAttributesInModel(Map<String, Object> model, HttpServletRequest request) {
        model.put("arrival", request.getSession().getAttribute("arrival"));
        model.put("departure", request.getSession().getAttribute("departure"));
        model.put("guests", request.getSession().getAttribute("guests"));
        model.put("category", request.getSession().getAttribute("category"));
    }

    static void setAttributesFromManager(Map<String, Object> model, HttpServletRequest request, Order order) {
        HttpSession session = request.getSession();
        session.setAttribute("arrival", order.getArrival());
        session.setAttribute("departure", order.getDeparture());
        session.setAttribute("guests", order.getGuests());
        session.setAttribute("category", order.getCategory());
    }

    public static LocalDate getDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }
}
