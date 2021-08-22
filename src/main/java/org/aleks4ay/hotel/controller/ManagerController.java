package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.exception.NoMoneyException;
import org.aleks4ay.hotel.exception.NotEmptyRoomException;
import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.service.OrderService;
import org.aleks4ay.hotel.service.ProposalService;
import org.aleks4ay.hotel.service.RoomService;
import org.aleks4ay.hotel.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ManagerController {
    private static final Logger log = LogManager.getLogger(ManagerController.class);
    private static final int POSITION_ON_PAGE = 4;

    @Autowired
    private ProposalService proposalService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping("/manager")
    public String getUserPage(Map<String, Object> model) {
        return "redirect:/manager/proposal";
    }


    @GetMapping("/manager/room")
    public String getRooms(Map<String, Object> model, HttpServletRequest request) {
        int page = initPageAttributes(model, request);
        model.put("categories", Category.values());
        HttpSession session = request.getSession();
        List<Room> roomList = roomService.getRooms(request);
        roomList = roomService.setSorting(roomList, request);
        roomList = roomService.doPagination(POSITION_ON_PAGE, page, roomList);
        model.put("rooms", roomList);
//        if (!roomList.isEmpty()) {
//        }
        model.putIfAbsent("action", "room");
        model.put("arrival", session.getAttribute("arrival"));
        model.put("departure", session.getAttribute("departure"));
        model.put("guests", session.getAttribute("guests"));
        model.put("category", session.getAttribute("category"));
        return "managerPage";
    }

    @GetMapping("/manager/room/proposal")
    public String doProposalManage(@RequestParam Long prId, Map<String, Object> model, HttpServletRequest request) {
        Proposal proposal = proposalService.getById(prId).get();
        HttpSession session = request.getSession();
        session.setAttribute("arrival", proposal.getArrival());
        session.setAttribute("departure", proposal.getDeparture());
        session.setAttribute("guests", proposal.getGuests());
        session.setAttribute("category", proposal.getCategory());
        model.put("prId", prId);
        model.put("action", "manageProposal");
        return getRooms(model, request);
    }

    @GetMapping("/manager/newOrder")
    private String saveNewOrder(@RequestParam int room, @RequestParam long prId,
                                Map<String, Object> model, HttpServletRequest request) {

        Proposal prop = proposalService.getById(prId).get();
        User user = userService.getById(prop.getUser().getId()).get();
        try {
            Optional<Order> order = orderService.createFromManager(room, prop, user);
            log.info("Was create new Order '{}'", order);
        } catch (NotEmptyRoomException e) {
            request.getSession().setAttribute("arrival", prop.getArrival());
            request.getSession().setAttribute("departure", prop.getDeparture());
            Optional<Room> roomOptional = roomService.getByNumber(room);
            model.put("roomOccupiedMessage", e);
            return doProposalManage(prop.getId(), model, request);
        }
        return "redirect:/manager/proposal";
    }

    @PostMapping("/manager/room/date")
    public String setDate(Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        Utils.parseDate(model, request);
        return getRooms(model, request);
    }

    @PostMapping("/manager/room/sort")
    public String setSort(@RequestParam String sortMethod, Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        request.getSession().setAttribute("sortMethod", sortMethod);
        return getRooms(model, request);
    }

    @GetMapping("/manager/order")
    private String getOrderPage(Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        List<Order> orderList = orderService.getAll();
        model.put("orders", orderList);
        model.put("action", "order");
        return "managerPage";
    }


    @GetMapping("/manager/proposal")
    private String getProposalPage(Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        List<Proposal> proposalList = proposalService.getAll();
        model.put("proposals", proposalList);
        model.put("action", "proposal");
        return "managerPage";
    }


    @PostMapping("/manager/filter")
    private String doFiltering(Map<String, Object> model, HttpServletRequest request) {
        Utils.doFiltering(model, request);
        return "redirect:/manager/room";
    }

    private int initPageAttributes(Map<String, Object> model, HttpServletRequest request) {
        model.put("userType", "manager");
        return Utils.initPageAttributes(model, request, POSITION_ON_PAGE);
    }
}