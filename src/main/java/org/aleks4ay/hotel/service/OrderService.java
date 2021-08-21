package org.aleks4ay.hotel.service;

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
        return orders;
    }

    public List<Order> getAllByUser(User user) {
        return orderRepo.findAllByUser(user);
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

/*    public boolean updateStatus(Order.Status status, long id) {
        Connection conn = ConnectionPool.getConnection();
        OrderDao orderDao = new OrderDao(conn);
        boolean result = orderDao.updateStatus(status.toString(), id);
        ConnectionPool.closeConnection(conn);
        return result;
    }*/

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
