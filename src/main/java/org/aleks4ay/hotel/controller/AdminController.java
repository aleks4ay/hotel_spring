package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.exception.AlreadyException;
import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AdminController {
    private static final Logger log = LogManager.getLogger(AdminController.class);
    private static final int POSITION_ON_PAGE_ROOM = 3;
    private static final int POSITION_ON_PAGE = 6;

    @Value("${uploadPath}")
    private String uploadPath;

    private RoomService roomService;
    private OrderService orderService;
    private UserService userService;

    public AdminController(RoomService roomService, OrderService orderService, UserService userService) {
        this.roomService = roomService;
        this.orderService = orderService;
        this.userService = userService;
    }


    @GetMapping("/admin")
    public String getUserPage(Map<String, Object> model) {
        model.put("itemOnPage", POSITION_ON_PAGE);
        return "index";
    }

    @GetMapping("/admin/room")
    public String getRoomPage(Map<String, Object> model, HttpServletRequest request) {
        Page<Room> roomList = roomService.findAll(getPageNumber(model, request) - 1, POSITION_ON_PAGE_ROOM, "number");
        model.put("rooms", roomList);
        model.put("categories", Category.values());
        model.put("action", "room");
        model.put("itemOnPage", POSITION_ON_PAGE_ROOM);
        return "a_room";
    }


    @GetMapping("/admin/newRoom")
    public String newRoom(@ModelAttribute Room room) {
        return "redirect:/admin/room";
    }

    @PostMapping("/admin/newRoom")
    public String saveNewRoom(@ModelAttribute Room room, Map<String, Object> model, HttpServletRequest request) {
        try {
            roomService.save(room);
            String startLog = room.getId() > 0L ? "update" : "save new";
            log.info("Was {} Room '{}'", startLog, room);
        } catch (AlreadyException e) {
            roomService.addOldValues(model, room);
            return getRoomPage(model, request);
        }
        return "redirect:/admin/room?pg=" + getPageNumber(model, request);
    }

    @GetMapping("/admin/room/change")
    public String changeRoom(@RequestParam Long id, Map<String, Object> model, @RequestParam int pg) {
        model.put("categories", Category.values());
        model.put("room", roomService.getById(id));
        model.put("pg", pg);
        return "a_changeRoom";
    }

    @GetMapping("/admin/user/change")
    public String changeUser(@RequestParam Long id, @RequestParam int pg) {
        User user = userService.getById(id);
        user.setActive(!user.isActive());
        userService.update(user);
        log.info("Was change status of User '{}'", user.getLogin());
        return "redirect:/admin/user?pg=" + pg;
    }

    @GetMapping("/admin/user")
    public String getUserPage(Map<String, Object> model, HttpServletRequest request) {
        int page = getPageNumber(model, request);
        model.put("users", userService.findAll(page - 1, POSITION_ON_PAGE, "login"));
        model.put("action", "user");
        return "a_user";
    }

    @GetMapping("/admin/order")
    public String getOrderPage(Map<String, Object> model, HttpServletRequest request) {
        int page = getPageNumber(model, request);
        model.put("orders", orderService.findAll(page - 1, POSITION_ON_PAGE, Sort.by("id")));
        model.put("action", "order");
        return "a_order";
    }

    private int getPageNumber(Map<String, Object> model, HttpServletRequest request) {
        int page = request.getParameter("pg") != null ? Integer.parseInt(request.getParameter("pg")) : 1;
        model.put("itemOnPage", POSITION_ON_PAGE);
        model.put("pg", page);
        model.put("userType", "admin");
        return page;
    }
}
