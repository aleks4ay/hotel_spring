package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.Room;
import org.aleks4ay.hotel.service.OrderService;
import org.aleks4ay.hotel.service.RoomService;
import org.aleks4ay.hotel.service.ScheduleService;
import org.aleks4ay.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    private static final int POSITION_ON_PAGE = 10;


    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;



    @GetMapping("/admin")
    public String getUserPage(Map<String, Object> model) {


/*        OrderService orderService = new OrderService();
        final Optional<User> user1 = orderService.userService.getByLogin("12");
        final Optional<Order> order = orderService.getById(1000006L);
        System.out.println(order);
        List<Order> orders = orderService.getAll();
        orders.forEach(System.out::println);*/


//        Room room = roomService.getById(20L).get();
//        Schedule schedule = new Schedule(LocalDate.of(2021, 8, 13), LocalDate.of(2021, 8, 18), Schedule.RoomStatus.BOOKED, room);
//        boolean b = scheduleService.checkRoom(schedule);
//        System.out.println("b=" + b);

/*        Room room = new Room(201, Category.SUITE, 2, "Комната с видом на море", 2200.0);
        System.out.println("before:" + room);
        Schedule schedule = new Schedule(LocalDate.of(2021, 8, 11), LocalDate.of(2021, 8, 13), Schedule.RoomStatus.BOOKED, room);
        room.addSchedule(schedule);

        schedule.setRoom(room);
        scheduleService.create(schedule);
        System.out.println("after: " + room);*/

        System.out.println("It's worked");

        return "index";
    }

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
