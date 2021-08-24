package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
class ManagerController {
    private static final Logger log = LogManager.getLogger(ManagerController.class);
    private static final int POSITION_ON_PAGE_ROOM = 3;
    private static final int POSITION_ON_PAGE = 6;

    private RoomService roomService;
    private OrderService orderService;

    @Autowired
    public ManagerController(RoomService roomService, OrderService orderService) {
        this.roomService = roomService;
        this.orderService = orderService;
    }

    @GetMapping("/manager")
    public String getUserPage(Map<String, Object> model) {
        return "redirect:/manager/proposal";
    }

    @GetMapping("/manager/order")
    public String getOrderPage(Map<String, Object> model, HttpServletRequest request) {
        Sort sort = Sort.by("status").and(Sort.by("id").descending());
        model.put("orders", orderService.findAll(getPageNumber(model, request) - 1, POSITION_ON_PAGE, sort));
        model.put("action", "order");
        return "m_order";
    }

    @GetMapping("/manager/room")
    public String getRooms(Map<String, Object> model, HttpServletRequest request) {
        int page = getPageNumber(model, request);
        Utils.setAttributesInModel(model, request);
        model.put("categories", Category.values());
        List<Room> rooms = roomService.getRoomsWithFilter(request);
        rooms = roomService.setSorting(rooms, (String) request.getSession().getAttribute("sortMethod"));
        model.put("rooms", roomService.doPagination(POSITION_ON_PAGE_ROOM, page, rooms));
        model.putIfAbsent("action", "room");
        model.put("itemOnPage", POSITION_ON_PAGE_ROOM);
        return "m_room";
    }

    @GetMapping("/manager/room/order")
    public String doProposalManage(@RequestParam Long ordId, Map<String, Object> model, HttpServletRequest request) {
        Utils.setAttributesFromManager(model, request, orderService.getById(ordId));
        model.put("ordId", ordId);
        model.put("action", "manageProposal");
        return getRooms(model, request);
    }

    @GetMapping("/manager/newOrder")
    public String saveNewOrder(@RequestParam long roomId, @RequestParam long ordId) {
        Order order = orderService.getById(ordId);
        order.setRoomDeeply(roomService.getById(roomId));
        order.setStatus(Order.Status.BOOKED);
        orderService.save(order);
        log.info("Was processed Order '{}'", order);
        return "redirect:/manager/order";
    }

    @PostMapping("/manager/room/sort")
    public String setSort(@RequestParam String sortMethod, HttpServletRequest request) {
        request.getSession().setAttribute("sortMethod", sortMethod);
        return "redirect:/manager/room";
    }

    @PostMapping("/manager/room/filter")
    public String doFiltering(Map<String, Object> model, HttpServletRequest request) {
        getPageNumber(model, request);
        Utils.doFiltering(model, request);
        return "redirect:/manager/room";
    }

    private int getPageNumber(Map<String, Object> model, HttpServletRequest request) {
        model.put("userType", "manager");
        return Utils.initPageAttributes(model, request, POSITION_ON_PAGE);
    }
}