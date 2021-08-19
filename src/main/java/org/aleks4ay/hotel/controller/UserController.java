package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.exception.NotFoundException;
import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
//@RequestMapping("/user")
public class UserController {
    private static final int POSITION_ON_PAGE = 5;


    @Autowired
    private ProposalService proposalService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String getUserPage(Map<String, Object> model) {
        return "redirect:/user/room";
    }


    @GetMapping("/user/room")
    public String getRooms(Map<String, Object> model, HttpServletRequest request) {
        int page = initPageAttributes(model, request);
        model.put("categories", Category.values());
        HttpSession session = request.getSession();
        List<Room> roomList = roomService.getRooms(request);
        roomList = roomService.doPagination(POSITION_ON_PAGE, page, roomList);
        model.put("rooms", roomList);
        model.put("action", "room");
        model.put("arrival", session.getAttribute("arrival"));
        model.put("departure", session.getAttribute("departure"));
        return "userPageRoom";
    }

    @PostMapping("/user/room/date")
    public String setDate(Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        parseDate(model, request);
        System.out.println("arrival=" + model.get("arrival"));
        return getRooms(model, request);
    }

    @GetMapping("/user/booking")
    private String doBooking(@RequestParam Long id, Map<String, Object> model, HttpServletRequest request) {
        if (request.getRemoteUser() == null) {
            return "index";
        }
        Optional<Room> roomOptional = roomService.getById(id);
        final OrderDto orderDto = userService.createOrderDto(request, roomOptional.get());

        model.put("orderDto", orderDto);
        return "newOrder";
    }

    @PostMapping("/user/newOrder")
    private String saveNewOrder(@RequestParam int number, Map<String, Object> model, HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate arrival = LocalDate.parse(request.getParameter("arrival"), formatter);
        LocalDate departure = LocalDate.parse(request.getParameter("departure"), formatter);
        Optional<User> userOptional = userService.getByLogin(request.getRemoteUser());
        orderService.create(number, arrival, departure, userOptional.get());
        return "redirect:/user/account/order";
    }


    @GetMapping("user/account/order")
    private String getOrderPage(Map<String, Object> model, HttpServletRequest request) {
        int page = initPageAttributes(model, request);
        User user = userService.getByLogin(request.getRemoteUser()).orElse(null);
        List<Order> orderList = orderService.getAllByUser(user);
//        orderList = orderService.doPagination(POSITION_ON_PAGE, page, orderList);
        user.setOrders(orderList);

        model.put("orders", orderList);
        model.put("action", "order");
        return "userPage";
    }

    @GetMapping("user/account/proposal")
    private String getProposalPage(Map<String, Object> model, HttpServletRequest request) {
        int page = initPageAttributes(model, request);
        User user = userService.getByLogin(request.getRemoteUser()).orElse(null);
        List<Proposal> proposalList = proposalService.getAllByUser(user);

        model.put("proposals", proposalList);
        model.put("action", "proposal");
        return "userPage";
    }

    @GetMapping("user/account/bill")
    private String getBillPage(Map<String, Object> model, HttpServletRequest request) {
        int page = initPageAttributes(model, request);
        User user = userService.getByLogin(request.getRemoteUser()).orElse(null);
        model.put("action", "bill");
        model.put("bill", user.getBill());
        return "userPage";
    }


    @PostMapping("/user/account/changeBill")
    private String doChangeBill(@RequestParam int addBill, HttpServletRequest request) {
//        int page = initPageAttributes(model, request);
        User user = userService.getByLogin(request.getRemoteUser()).orElse(null);

//        int number = Integer.parseInt(request.getParameter("addBill"));
        user.setBill(user.getBill() + addBill);
        userService.update(user);
        return "redirect:/user/account/bill";
    }


   /* public String execute(HttpServletRequest request) {
        String userType = (String) request.getAttribute("userType");

        User user = (User) request.getSession().getAttribute("user");

        String action = request.getParameter("action");
        System.out.println("ACTION = " + action);
        request.setAttribute("action", action);
        request.setAttribute("itemOnPage", POSITION_ON_PAGE);
        request.setAttribute("categories", Category.values());

        //user?action=account when 'NotUser'
        if(     (userType.equalsIgnoreCase("guest") || user == null)
                && !action.contains("room")
                && !action.equalsIgnoreCase("filter") ) {
            return "/WEB-INF/index.jsp";
        }
        //user?action=room
        if (action.equalsIgnoreCase("room") || action.contains("room")) {
            return getRoom(request);
        }
        //user?action=setDate
        if (action.equalsIgnoreCase("setDate")) {
            return setDate(request);
        }
        //user?action=booking
        if (action.equalsIgnoreCase("booking")) {
            return doBooking(request);
        }
        //user?action=newProposal
        if (action.equalsIgnoreCase("newProposal")) {
            return doNewProposal(request, user);
        }
        //user?action=newOrder&id=22
        if (action.equalsIgnoreCase("newOrder")) {
            return doNewOrder(request, user);
        }
        //user?action=filter
        if (action.equalsIgnoreCase("filter")) {
            return doFiltering(request);
        }
        //user?action=changeBill
        if (action.equalsIgnoreCase("changeBill")) {
            return doChangeBill(request, user);
        }
        //user?action=account&ap=***
        if (action.equalsIgnoreCase("account")) {
            return doAccount(request, user);
        }
        return "WEB-INF/jsp/userPage.jsp";
    }

    private String setDate(HttpServletRequest request) {
        return "redirect:/user?action=room";
    }







    private String doFiltering(HttpServletRequest request) {
        List<String> filters = new ArrayList<>();

        String filterButtonName = request.getParameter("filter");

        if (filterButtonName != null && filterButtonName.equalsIgnoreCase("filterCansel")) {
            request.removeAttribute("category");
            request.removeAttribute("guests");
            request.getSession().removeAttribute("category");
            request.getSession().removeAttribute("guests");
            request.getSession().removeAttribute("arrival");
            request.getSession().removeAttribute("departure");
        } else {
//        System.out.println("filterButtonName=" + filterButtonName);
            String categoryString = request.getParameter("filter_category");
            String guestsString = request.getParameter("filter_guests");

            request.setAttribute("category", request.getParameter("filter_category"));
            request.setAttribute("guests", Integer.parseInt(guestsString));

            filters.add(" guests = " + guestsString);
            filters.add(" category = '" + categoryString + "'");
            request.setAttribute("filters", filters);
        }
        return "redirect:/user?action=room";
    }


    private String doNewProposal(HttpServletRequest request, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStartString = request.getParameter("arrival");
        String dateEndString = request.getParameter("departure");
        LocalDate dateStart, dateEnd;
        if (dateEndString.isEmpty() || dateEndString.isEmpty()){
            dateStart = LocalDate.now();
            dateEnd = dateStart.plusDays(1);
        } else {
            dateStart = LocalDate.parse(dateStartString, formatter);
            dateEnd = LocalDate.parse(dateEndString, formatter);
        }

        int guests = Integer.parseInt(request.getParameter("field1"));
        Category category = Category.valueOf(request.getParameter("field2"));
        new ProposalService().save(new Proposal(dateStart, dateEnd, guests, category, user));
        return "redirect:/user?action=account&ap=proposal";
    }



*/

    private int initPageAttributes(Map<String, Object> model, HttpServletRequest request) {
        int page = 1;
        if (request.getParameter("pg") != null) {
            page = Integer.parseInt(request.getParameter("pg"));
        }
        model.put("itemOnPage", POSITION_ON_PAGE);
        model.put("pg", page);

        Object userObject = request.getRemoteUser();

        String userType;
        if(userObject == null) {
            userType = "guest";
        } else {
            userType = "user";
        }
        model.put("userType", userType);

        return page;
    }



    private void parseDate(Map<String, Object> model, HttpServletRequest request) {
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
}