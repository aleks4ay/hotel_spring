package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.model.Order;
import org.aleks4ay.hotel.model.Room;
import org.aleks4ay.hotel.model.Schedule;
import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.repository.OrderRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public static void main(String[] args) {
/*
        OrderService orderService = new OrderService();
        final Optional<User> user1 = orderService.userService.getByLogin("12");
        final Optional<Order> order = orderService.getById(1000006L);
        System.out.println(order);
        List<Order> orders = orderService.getAll();
        orders.forEach(System.out::println);
*/


    }

    public Optional<Order> getById(Long id) {
        Optional<Order> optional = orderRepo.findById(id);
/*        if (optional.isPresent()) {
            Order order = optional.get();
            order.setUser(userService.getById(order.getUser().getId()).orElse(null));
            order.setRoom(roomService.getById(order.getRoom().getId()).orElse(null));
            order.setSchedule(scheduleService.getById(order.getSchedule().getId()).orElse(null));
        }*/
        return optional;
    }

    public List<Order> getAll() {
        List<Order> orders = (List<Order>) orderRepo.findAll();
/*
        Map<Long, Room> rooms = roomService.getAllAsMap();
        Map<Long, User> users = userService.getAllAsMap();
        Map<Long, Schedule> schedules = scheduleService.getAllAsMap();

        for (Order o : orders) {
            o.setUser(users.get(o.getUser().getId()));
            o.setRoom(rooms.get(o.getRoom().getId()));
            o.setSchedule(schedules.get(o.getSchedule().getId()));
        }
*/
        return orders;
    }

    public List<Order> getAllByUser(User user) {
        return getAll()
                .stream()
                .filter(order -> order.getUser().getId() == user.getId())
                .collect(Collectors.toList());
    }

/*    public boolean delete(Long id) {
        Connection conn = ConnectionPool.getConnection();
        OrderDao orderDao = new OrderDao(conn);
        boolean result = orderDao.delete(id);
        ConnectionPool.closeConnection(conn);
        return result;
    }*/

    @Transactional
    public Optional<Order> create(long room_id, LocalDate dateStart, LocalDate dateEnd, User user) {

        Order builtOrder = buildOrder(room_id, dateStart, dateEnd, user);

        if (scheduleService.checkRoom(builtOrder.getSchedule())) {
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

    private Order buildOrder(long room_id, LocalDate dateStart, LocalDate dateEnd, User user) {

        Room room = roomService.getById(room_id).orElse(null);

        Order tempOrder = new Order();
        tempOrder.setRegistered(LocalDateTime.now());
//        user.addOrder(tempOrder);
        Order.Status orderStatus = Order.Status.CONFIRMED;
        Schedule.RoomStatus roomStatus = Schedule.RoomStatus.BOOKED;
        if (user.isManager()) {
            orderStatus = Order.Status.NEW;
            roomStatus = Schedule.RoomStatus.RESERVED;
        }
        Schedule schedule = new Schedule(dateStart, dateEnd, roomStatus, room);

//        tempOrder.setRoom(room);
        tempOrder.setSchedule(schedule);
        tempOrder.setUser(user);
        user.addOrder(tempOrder);
        tempOrder.setStatus(orderStatus);
        return tempOrder;
    }
}
