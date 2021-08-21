package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.exception.NoMoneyException;
import org.aleks4ay.hotel.exception.NotEmptyRoomException;
import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.repository.OrderRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private static final Logger log = LogManager.getLogger(OrderService.class);

    private OrderRepo orderRepo;
    private RoomService roomService;
    private ScheduleService scheduleService;

    @Autowired
    public void setOrderRepo(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    public Optional<Order> getById(Long id) {
        Optional<Order> optional = orderRepo.findById(id);
        return optional;
    }

    public List<Order> getAll() {
        List<Order> orders = (List<Order>) orderRepo.findAll();
        orders.sort(comparing(Order::getId));
        return orders;
    }

    public List<Order> getAllByUser(User user) {
        List<Order> orders = orderRepo.findAllByUser(user);
        orders.sort(comparing(Order::getId));
        return orders;
    }

    @Transactional
    public Optional<Order> save(Order order) {
        return Optional.of(orderRepo.save(order));
    }

    @Transactional
    public Optional<Order> pay(Order order) {
        Double cost = order.getCost();
        Double bill = order.getUser().getBill();
        if (bill >= cost) {
            order.getUser().setBill(order.getUser().getBill() - cost);
            order.setStatus(Order.Status.PAID);
            return Optional.of(orderRepo.save(order));
        } else {
            throw new NoMoneyException("Sorry, there are not enough funds in your account");
        }
    }

    @Transactional
    public Optional<Order> create(int roomNumber, LocalDate dateStart, LocalDate dateEnd, User user) {

        Order builtOrder = buildOrder(roomNumber, dateStart, dateEnd, user);
        boolean isEmpty;
        try {
            isEmpty = scheduleService.checkRoom(builtOrder.getSchedule());
        } catch (NotEmptyRoomException e) {
            throw e;
        }
        if (isEmpty) {
            return Optional.of(orderRepo.save(builtOrder));
        }
        return Optional.empty();
    }

    private Order buildOrder(int roomNumber, LocalDate dateStart, LocalDate dateEnd, User user) {

        Room room = roomService.getByNumber(roomNumber).orElse(null);

        Order tempOrder = new Order();
        tempOrder.setRegistered(LocalDateTime.now());
        Order.Status orderStatus = Order.Status.CONFIRMED;
        Schedule.RoomStatus roomStatus = Schedule.RoomStatus.BOOKED;
        if (user.isManager()) {
            orderStatus = Order.Status.NEW;
            roomStatus = Schedule.RoomStatus.RESERVED;
        }
        Schedule schedule = new Schedule(dateStart, dateEnd, roomStatus, room);

        tempOrder.setSchedule(schedule);
        tempOrder.setUser(user);
        user.addOrder(tempOrder);
        tempOrder.setStatus(orderStatus);
        return tempOrder;
    }

    public List<Order> doPagination(int positionOnPage, int page, List<Order> entities) {
        UtilService utilService = new UtilService<Order>();
        entities.sort(comparing(Order::getId));
        List<Order> result = utilService.doPagination(positionOnPage, page, entities);
        return result;
    }

    public OrderDto createOrderDto(HttpServletRequest request, Room room) {
        HttpSession session = request.getSession();
        LocalDate dateStart = null;
        LocalDate dateEnd = null;
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

        return new OrderDto(room.getNumber(), room.getCategory(), room.getGuests(), room.getDescription(),
                dateStart, dateEnd, room.getPrice());
    }
}
