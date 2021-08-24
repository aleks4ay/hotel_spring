package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.exception.NoMoneyException;
import org.aleks4ay.hotel.exception.NotEmptyRoomException;
import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private static final int POSITION_ON_PAGE_ROOM = 3;
    private static final int POSITION_ON_PAGE = 6;

    private RoomService roomService;
    private OrderService orderService;
    private UserService userService;
    private InvoiceService invoiceService;

    @Autowired
    public UserController(RoomService roomService, OrderService orderService,
                          UserService userService, InvoiceService invoiceService) {
        this.roomService = roomService;
        this.orderService = orderService;
        this.userService = userService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/user")
    public String getUserPage(Map<String, Object> model) {
        return "redirect:/user/room";
    }

    @GetMapping("/user/room")
    public String getRooms(Map<String, Object> model, HttpServletRequest request) {
        int page = getPageNumber(model, request);
        Utils.setAttributesInModel(model, request);
        model.put("categories", Category.values());
        List<Room> rooms = roomService.getRoomsWithFilter(request);
        rooms = roomService.setSorting(rooms, (String) request.getSession().getAttribute("sortMethod"));
        model.put("rooms", roomService.doPagination(POSITION_ON_PAGE_ROOM, page, rooms));
        model.putIfAbsent("action", "room");
        model.put("itemOnPage", POSITION_ON_PAGE_ROOM);
        return "u_room";
    }

    @PostMapping("user/room/sort")
    public String setSort(@RequestParam String sortMethod, Map<String, Object> model, HttpServletRequest request) {
        getPageNumber(model, request);
        request.getSession().setAttribute("sortMethod", sortMethod);
        return "redirect:/user/room";
    }

    @GetMapping("/user/account/booking")
    public String doBooking(@RequestParam Long id, Map<String, Object> model, HttpServletRequest request) {
        model.put("orderDto", orderService.createOrderDto(request, roomService.getById(id)));
        return "u_newOrder";
    }


    @PostMapping("user/account/newProposal")
    public String saveNewProposal(@ModelAttribute OrderDto orderDto, HttpServletRequest request) {
        Order order = orderService.saveOrderProposal(orderDto, userService.getByLogin(request.getRemoteUser()));
        log.info("Was create new Order '{}'", order);
        return "redirect:/user/account/order";
    }

    @PostMapping("/user/account/newOrder")
    public String saveNewOrder(@ModelAttribute OrderDto orderDto,
                                Map<String, Object> model, HttpServletRequest request) {
        Order order = orderService.create(orderDto, userService.getByLogin(request.getRemoteUser()));
        try {
            orderService.saveOrderIfEmpty(order);
            log.info("Was create new Order '{}'", order);
            Invoice invoice = invoiceService.save(new Invoice(LocalDateTime.now(), Invoice.Status.NEW, order));
            log.info("Was create new Invoice '{}'", invoice);
        } catch (NotEmptyRoomException e) {
            request.getSession().setAttribute("arrival", UtilService.getDate(orderDto.getArrivalString()));
            request.getSession().setAttribute("departure", UtilService.getDate(orderDto.getDepartureString()));
            model.put("roomOccupiedMessage", e);
            return doBooking(roomService.getByNumber(orderDto.getNumber()).getId(), model, request);
        }
        return "redirect:/user/account/order";
    }

    @GetMapping("user/account/order")
    public String getOrderPage(Map<String, Object> model, HttpServletRequest request) {
        int page = getPageNumber(model, request);
        long id = userService.getByLogin(request.getRemoteUser()).getId();
        Page<Order> orders = orderService.getAllByUserId(id, page - 1, POSITION_ON_PAGE, Sort.by("id").descending());
        model.put("orders", orders);
        model.put("action", "order");
        return "u_order";
    }

    @GetMapping("/user/account/order/change")
    public String changeOrderPage(@RequestParam Long id, Map<String, Object> model, HttpServletRequest request) {
        model.put("categories", Category.values());
        model.put("order", orderService.getById(id));
        model.put("bill", userService.getByLogin(request.getRemoteUser()).getBill());
        return "u_changeOrder";
    }

    @PostMapping("/user/account/changeOrder")
    public String changeOrderStatus(@RequestParam Long id, Map<String, Object> model, HttpServletRequest request) {
        Order order = orderService.getById(id);
        if (request.getParameter("changeStatus").equalsIgnoreCase("confirm")) {
            order.setStatus(Order.Status.CONFIRMED);
            Invoice invoice = invoiceService.save(new Invoice(LocalDateTime.now(), Invoice.Status.NEW, order));
            log.info("Was confirmed Order '{}'", order);
            log.info("Was create new Invoice '{}'", invoice);
        } else {
            try {
                orderService.pay(order);
            } catch (NoMoneyException e) {
                model.put("noMoneyMessage", e);
                return changeOrderPage(id, model, request);
            }
        }
        return "redirect:/user/account/order/change?id=" + id;
    }


    @GetMapping("user/account/bill")
    public String getBill(@AuthenticationPrincipal User user, Map<String, Object> model, HttpServletRequest request) {
        getPageNumber(model, request);
        model.put("action", "bill");
        model.put("bill", user.getBill());
        return "u_bill";
    }


    @PostMapping("/user/account/changeBill")
    public String doChangeBill(@AuthenticationPrincipal User user, @RequestParam int addBill) {
        user.addBill(addBill);
        userService.update(user);
        log.info("User {} has increased his account by {} grn.", user.getLogin(), user.getBill());
        return "redirect:/user/account/bill";
    }

    @PostMapping("/user/room/filter")
    public String doFiltering(Map<String, Object> model, HttpServletRequest request) {
        getPageNumber(model, request);
        Utils.doFiltering(model, request);
        return "redirect:/user/room";
    }

    private int getPageNumber(Map<String, Object> model, HttpServletRequest request) {
        if(request.getRemoteUser() == null) {
            model.put("userType", "guest");
        } else {
            model.put("userType", "user");
        }
        return Utils.initPageAttributes(model, request, POSITION_ON_PAGE);
    }
}