package org.aleks4ay.hotel.controller;

import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Room;
import org.aleks4ay.hotel.service.OrderService;
import org.aleks4ay.hotel.service.RoomService;
import org.aleks4ay.hotel.service.ScheduleService;
import org.aleks4ay.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private static final int POSITION_ON_PAGE = 5;


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


    @GetMapping("/user/room")
    public String getRooms(HttpServletRequest request) {
        List<Room> roomList = roomService.getRooms(request);
        int page = 1;
        if (request.getAttribute("pg") != null) {
            page = (int) request.getAttribute("pg");
        }
        roomList = roomService.doPagination(POSITION_ON_PAGE, page, roomList);
        request.setAttribute("rooms", roomList);
        request.setAttribute("itemOnPage", POSITION_ON_PAGE);
        request.setAttribute("pg", page);
        return "userPageRoom";
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


    private String doChangeBill(HttpServletRequest request, User user) {
        int number = Integer.parseInt(request.getParameter("addBill"));
        user.setBill(user.getBill() + number);
        new UserService().update(user);
        return "redirect:/user?action=account&ap=bill";
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


    private String doAccount(HttpServletRequest request, User user) {
        String actionPage = request.getParameter("ap");
        //user?action=account&ap=
        if (actionPage == null) {
            actionPage = "order";
        }
        //user?action=account&ap=order
        if (actionPage.equalsIgnoreCase("order")) {
            List<Order> orderList = new OrderService().getAllByUser(user);
            user.setOrders(orderList);
            request.setAttribute("orders", orderList);
            //user?action=account&ap=proposal
        } else if (actionPage.equalsIgnoreCase("proposal")) {
            List<Proposal> proposalList = new ProposalService().getAllByUser(user);
            request.setAttribute("proposals", proposalList);
            //user?action=account&ap=bill
        } else if (actionPage.equalsIgnoreCase("bill")) {
            request.setAttribute("bill", user.getBill());

        }
        request.setAttribute("ap", actionPage);
        return "WEB-INF/jsp/userPage.jsp";
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
        new ProposalService().create(new Proposal(dateStart, dateEnd, guests, category, user));
        return "redirect:/user?action=account&ap=proposal";
    }


    private String doNewOrder(HttpServletRequest request, User user) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStartString = request.getParameter("date_arrival");
        String dateEndString = request.getParameter("date_departure");
        LocalDate dateStart = LocalDate.parse(dateStartString, formatter);
        LocalDate dateEnd = LocalDate.parse(dateEndString, formatter);

        long room_id = Long.parseLong(request.getParameter("id"));
        new OrderService().create(room_id, dateStart, dateEnd, user);
        return "redirect:/user?action=account&ap=order";
    }


    private String doBooking(HttpServletRequest request) {
//        System.out.println("Booking");
        User user = (User) request.getSession().getAttribute("user");
        HttpSession session = request.getSession();
//        request.setAttribute("action", "booking");
        long id = Long.parseLong(request.getParameter("id"));
        Order order = new Order();
        order.setUser(user);
        order.setRoom(new RoomService().getById(id).get());

        LocalDate dateStart = null;
        LocalDate dateEnd = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (session.getAttribute("arrival") != null) {
            dateStart = (LocalDate) session.getAttribute("arrival");
        }
        if (session.getAttribute("departure") != null) {
            dateEnd = (LocalDate) session.getAttribute("departure");
        }

        if (dateStart == null) {
            if (dateEnd == null) {
                dateStart = LocalDate.now();
                dateEnd = dateStart.plusDays(1);
            } else {
                dateStart = dateEnd.minusDays(1);
            }
        } else if (dateEnd == null) {
            dateEnd = dateStart.plusDays(1);
        }

        request.setAttribute("order", order);

        request.setAttribute("arrival", dateStart);
        request.setAttribute("departure", dateEnd);
        return "WEB-INF/jsp/userPage.jsp";
    }*/

}
