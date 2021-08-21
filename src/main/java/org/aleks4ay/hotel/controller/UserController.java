package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.exception.NoMoneyException;
import org.aleks4ay.hotel.exception.NotEmptyRoomException;
import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {
    private static final int POSITION_ON_PAGE = 4;

    @Autowired
    private ProposalService proposalService;
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
        roomList = roomService.setSorting(roomList, request);
        roomList = roomService.doPagination(POSITION_ON_PAGE, page, roomList);
        model.put("rooms", roomList);
        model.put("action", "room");
        model.put("arrival", session.getAttribute("arrival"));
        model.put("departure", session.getAttribute("departure"));
        model.put("guests", session.getAttribute("guests"));
        model.put("category", session.getAttribute("category"));
        return "userPageRoom";
    }

    @PostMapping("/user/room/date")
    public String setDate(Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        parseDate(model, request);
        return getRooms(model, request);
    }

    @PostMapping("user/room/sort")
    public String setSort(@RequestParam String sortMethod, Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        request.getSession().setAttribute("sortMethod", sortMethod);
        System.out.println("sort=" + sortMethod);
//        parseSort(model, request, sortMethod);
        return getRooms(model, request);
    }


    @GetMapping("/user/booking")
    private String doBooking(@RequestParam Long id, Map<String, Object> model, HttpServletRequest request) {
        if (request.getRemoteUser() == null) {
            return "index";
        }
        Optional<Room> roomOptional = roomService.getById(id);
        final OrderDto orderDto = orderService.createOrderDto(request, roomOptional.get());

        model.put("orderDto", orderDto);
        return "newOrder";
    }

    @PostMapping("/user/newOrder")
    private String saveNewOrder(@RequestParam int number, Map<String, Object> model, HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate arrival = LocalDate.parse(request.getParameter("arrival"), formatter);
        LocalDate departure = LocalDate.parse(request.getParameter("departure"), formatter);
        Optional<User> userOptional = userService.getByLogin(request.getRemoteUser());
        try {
            orderService.create(number, arrival, departure, userOptional.get());
        } catch (NotEmptyRoomException e) {
            request.getSession().setAttribute("arrival", arrival);
            request.getSession().setAttribute("departure", departure);
            Optional<Room> roomOptional = roomService.getByNumber(number);
            model.put("roomOccupiedMessage", e);
            return doBooking(roomOptional.get().getId(), model, request);
        }
        return "redirect:/user/account/order";
    }



    @GetMapping("user/account/order")
    private String getOrderPage(Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        User user = userService.getByLogin(request.getRemoteUser()).orElse(null);
        List<Order> orderList = orderService.getAllByUser(user);
        user.setOrders(orderList);

        model.put("orders", orderList);
        model.put("action", "order");
        return "userPage";
    }

    @GetMapping("/user/account/order/change")
    private String changeOrderPage(@RequestParam Long id, Map<String, Object> model, HttpServletRequest request) {
        final User user = userService.getByLogin(request.getRemoteUser()).orElse(null);
        final Optional<Order> orderOptional = orderService.getById(id);
        model.put("categories", Category.values());
        model.put("order", orderOptional.get());
        model.put("bill", user.getBill());
        return "changeOrder";
    }

    @PostMapping("/user/account/changeOrder")
    private String changeOrderStatus(@RequestParam Long id, Map<String, Object> model, HttpServletRequest request) {
        final Optional<Order> orderOptional = orderService.getById(id);
        String changeButtonName = request.getParameter("changeStatus");
        if (changeButtonName != null) {
            Order order = orderOptional.get();
            if (changeButtonName.equalsIgnoreCase("confirm")) {
                order.setStatus(Order.Status.CONFIRMED);
                orderService.save(order);
            } else if (changeButtonName.equalsIgnoreCase("pay")) {
                try {
                    orderService.pay(order);
                } catch (NoMoneyException e) {
                    model.put("noMoneyMessage", e);
                    return changeOrderPage(id, model, request);
                }
            }
        }
        return "redirect:/user/account/order/change?id=" + id;
    }


    @GetMapping("user/account/proposal")
    private String getProposalPage(Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        User user = userService.getByLogin(request.getRemoteUser()).orElse(null);
        List<Proposal> proposalList = proposalService.getAllByUser(user);

        model.put("proposals", proposalList);
        model.put("action", "proposal");
        return "userPage";
    }

    @PostMapping("/user/newProposal")
    private String saveNewProposal(@RequestParam int guests, @RequestParam Category category,
                                   Map<String, Object> model, HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate arrival = LocalDate.parse(request.getParameter("arrival"), formatter);
        LocalDate departure = LocalDate.parse(request.getParameter("departure"), formatter);
        Optional<User> userOptional = userService.getByLogin(request.getRemoteUser());

        Proposal proposal = new Proposal(arrival, departure, guests, category);
        System.out.println("proposal = " + proposal);

        proposalService.save(arrival, departure, guests, category, userOptional.get());
        return "redirect:/user/account/proposal";
    }

    @GetMapping("user/account/bill")
    private String getBillPage(Map<String, Object> model, HttpServletRequest request) {
        initPageAttributes(model, request);
        User user = userService.getByLogin(request.getRemoteUser()).orElse(null);
        model.put("action", "bill");
        model.put("bill", user.getBill());
        return "userPage";
    }


    @PostMapping("/user/account/changeBill")
    private String doChangeBill(@RequestParam int addBill, HttpServletRequest request) {
        User user = userService.getByLogin(request.getRemoteUser()).orElse(null);

        user.setBill(user.getBill() + addBill);
        userService.update(user);
        return "redirect:/user/account/bill";
    }

    @PostMapping("/user/filter")
    private String doFiltering(Map<String, Object> model, HttpServletRequest request) {

        String filterButtonName = request.getParameter("filter");

        if (filterButtonName != null && filterButtonName.equalsIgnoreCase("filterCansel")) {
            request.removeAttribute("category");
            request.removeAttribute("guests");
            model.remove("category");
            model.remove("guests");
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
        return "redirect:/user/room";
    }


    private int initPageAttributes(Map<String, Object> model, HttpServletRequest request) {
        int page = 1;
        Object sotrMethodObj = request.getSession().getAttribute("sortMethod");
        if (sotrMethodObj == null) {
            request.getSession().setAttribute("sortMethod", "byRoomNumber");
        }
        model.put("sortMethod", request.getSession().getAttribute("sortMethod"));

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

    private void parseSort(Map<String, Object> model, HttpServletRequest request, String sortMethod) {
        request.getSession().setAttribute("sortMethod", sortMethod);
    }

}