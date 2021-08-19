package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.service.OrderService;
import org.aleks4ay.hotel.service.RoomService;
import org.aleks4ay.hotel.service.ScheduleService;
import org.aleks4ay.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;


    @GetMapping("/shop")
    public String getProduct(Map<String, Object> model) {


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
        scheduleService.save(schedule);
        System.out.println("after: " + room);*/

        System.out.println("It's worked");

        return "index";
    }

}
