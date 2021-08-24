package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Order;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
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

    public static String saveImage(HttpServletRequest request, int number, String path) {
        String newFileName = "";
        Part filePart;
        try {
            filePart = request.getPart("image");
            String[] elements = filePart.getSubmittedFileName().split("\\.");
            String fileExtension = elements[elements.length - 1].toLowerCase();
            newFileName = getEmptyFileName(path, number + "." + fileExtension);
            InputStream is = filePart.getInputStream();
            byte[] buffer = new byte[is.available()];
            OutputStream os = new FileOutputStream(path + newFileName);
            is.read(buffer, 0, buffer.length);
            os.write(buffer, 0, buffer.length);
            is.close();
            os.close();
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        return newFileName;
    }

    private static String getEmptyFileName(String imagePath, String fileName) {
        File oldFile = new File(imagePath + fileName);
        if (oldFile.exists()) {
            for (int i = 1; ; i++) {
                String newName = fileName.replaceFirst("\\.", "(" + i + ").");
                if (! new File(imagePath + newName).exists()) {
                    return newName;
                }
            }
        }
        return fileName;
    }
}