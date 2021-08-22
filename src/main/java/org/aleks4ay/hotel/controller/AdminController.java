package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Order;
import org.aleks4ay.hotel.model.Room;
import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.service.OrderService;
import org.aleks4ay.hotel.service.RoomService;
import org.aleks4ay.hotel.service.UserService;
import org.aleks4ay.hotel.util.FileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AdminController {
    private static final Logger log = LogManager.getLogger(AdminController.class);
    private static final int POSITION_ON_PAGE_ROOM = 4;
    private static final int POSITION_ON_PAGE = 8;

    @Value("${web.upload-path}")
    private String uploadPath;

    @Autowired
    private RoomService roomService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;



    @GetMapping("/admin")
    public String getUserPage(Map<String, Object> model) {
        model.put("itemOnPage", POSITION_ON_PAGE);
        return "index";
    }

    @GetMapping("/admin/room")
    public String getRoomPage(Map<String, Object> model, HttpServletRequest request) {
        int page = initPageAttributes(model, request);
        List<Room> roomList = roomService.getRooms(request);
        roomList = roomService.doPagination(POSITION_ON_PAGE_ROOM, page, roomList);
        model.put("rooms", roomList);
        model.put("categories", Category.values());
        model.put("action", "room");
        model.put("itemOnPage", POSITION_ON_PAGE_ROOM);
        return "adminPage";
    }


    @GetMapping("/admin/newRoom")
    public String newRoom(@ModelAttribute Room room, Map<String, Object> model, HttpServletRequest request) {
        return "redirect:/admin/room";
    }

//    @ResponseBody
    @PostMapping("/admin/newRoom")
    public String saveNewRoom(@ModelAttribute Room room, Map<String, Object> model, HttpServletRequest request) {
        int page = initPageAttributes(model, request);
        if (room.getId() > 0L) {
            roomService.update(room);
            log.info("Was update Room '{}'", room);
            return "redirect:/admin/room?pg=" + page;
        }

        int number = room.getNumber();
        String description = room.getDescription();
        Double price = room.getPrice();
        int guests = room.getGuests();
        Category category = room.getCategory();

        if (roomService.checkNumber(number)) {
            model.put("roomExistMessage", "Room with this number exists!");
            model.put("oldNumber", number);
            model.put("oldDescription", description);
            model.put("oldGuests", guests);
            model.put("oldPrice", price);
            model.put("oldCategory", category);
            return getRoomPage(model, request);
        }
        roomService.save(room);
        log.info("Was save new Room '{}'", room);
        return "redirect:/admin/room?pg=" + page;
    }


    @GetMapping("/admin/room/change")
    public String changeRoom(@RequestParam Long id, Map<String, Object> model, @RequestParam int pg) {
        final Optional<Room> roomOptional = roomService.getById(id);
        model.put("categories", Category.values());
        model.put("room", roomOptional.get());
        model.put("pg", pg);
        return "changeRoom";
    }

    @GetMapping("/admin/user/change")
    public String changeUser(@RequestParam Long id, Map<String, Object> model, @RequestParam int pg) {
        final Optional<User> userOptional = userService.getById(id);
        userService.update(userOptional.get());
        log.info("Was change status of User '{}'", userOptional.get().getLogin());
        return "redirect:/admin/user?pg=" + pg;
    }


    @GetMapping("/admin/user")
    public String getUserPage(Map<String, Object> model, HttpServletRequest request) {
        int page = initPageAttributes(model, request);
        List<User> userList = userService.getAll();

        userList = userService.doPagination(POSITION_ON_PAGE, page, userList);
        model.put("users", userList);
        model.put("action", "user");
        return "adminPage";
    }

    @GetMapping("/admin/order")
    public String getOrderPage(Map<String, Object> model, HttpServletRequest request) {
        int page = initPageAttributes(model, request);
        List<Order> orderList = orderService.getAll();

        orderList = orderService.doPagination(POSITION_ON_PAGE, page, orderList);
        model.put("orders", orderList);
        model.put("action", "order");
        return "adminPage";
    }



    private int initPageAttributes(Map<String, Object> model, HttpServletRequest request) {
        int page = 1;
        if (request.getParameter("pg") != null) {
            page = Integer.parseInt(request.getParameter("pg"));
        }
        model.put("itemOnPage", POSITION_ON_PAGE);
        model.put("pg", page);
        model.put("userType", "admin");
        return page;
    }
}
