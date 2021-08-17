package org.aleks4ay.hotel.command;

import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Order;
import org.aleks4ay.hotel.model.Room;
import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.service.OrderService;
import org.aleks4ay.hotel.service.RoomService;
import org.aleks4ay.hotel.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

class AdminCommand  {
    private final static int POSITION_ON_PAGE = 10;

    public String execute(HttpServletRequest request) {
        if(request.getSession().getAttribute("user") == null) {
            return "/WEB-INF/index.jsp";
        }
 /*       String action = request.getParameter("action");
        request.setAttribute("itemOnPage", POSITION_ON_PAGE);

        if (action == null) {
            action = "user";
        }

        if (action.equalsIgnoreCase("newRoom")){
            int number = Integer.parseInt(request.getParameter("number"));
            String description = request.getParameter("description");
            Double price = Double.parseDouble(request.getParameter("price"));
            int guests = Integer.parseInt(request.getParameter("guests"));
            Category category = Category.valueOf(request.getParameter("category"));

            new RoomService().create(new Room(number, category, guests, description, price));
            return "redirect:/admin?action=room";
        }

        request.setAttribute("action", action);

        if (action.equalsIgnoreCase("user")) {
            UserService userService = new UserService();
            List<User> userList = userService.getAll();
            userList = userService.doPagination(POSITION_ON_PAGE, (int) request.getAttribute("pg"), userList);
            request.setAttribute("users", userList);

        } else if (action.equalsIgnoreCase("room")){
            RoomService roomService = new RoomService();
            List<Room> roomList = roomService.getAll();
//            request.getAttribute("pg");
            roomList = roomService.doPagination(POSITION_ON_PAGE, (int) request.getAttribute("pg"), roomList); // TODO: 10.08.2021
            request.setAttribute("rooms", roomList);
            request.setAttribute("categories", Category.values());

        } else if (action.equalsIgnoreCase("order")){
            List<Order> orderList = new OrderService().getAll();
            request.setAttribute("orders", orderList);

        }*/
        return "WEB-INF/jsp/adminPage.jsp";
    }
}