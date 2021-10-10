package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.exception.NotEmptyRoomException;
import org.aleks4ay.hotel.exception.NotFoundException;
import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final RoomService roomService;

    @Autowired
    public OrderService(OrderRepo orderRepo, RoomService roomService) {
        this.orderRepo = orderRepo;
        this.roomService = roomService;
    }

    public Order getById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new NotFoundException("Order #" + id + " not found"));
    }

    public Page<Order> findAll(int numPage, int rowsOnPage, Sort sort) {
        Pageable sortedPage = PageRequest.of(numPage, rowsOnPage, sort);
        return orderRepo.findAll(sortedPage);
    }

    public Page<Order> getAllByUserId(Long userId, int numPage, int rowsOnPage, Sort sort) {
        Pageable sortedPage = PageRequest.of(numPage, rowsOnPage, sort);
        return orderRepo.findAllByUserId(userId, sortedPage);
    }

    public Order save(Order order) {
        return orderRepo.save(order);
    }

    public Optional<Order> pay(Order order) {
        order.getUser().reduceBill(order.getCost());
        order.setStatus(Order.Status.PAID);
        order.getInvoice().setStatus(Invoice.Status.PAID);
        return Optional.of(orderRepo.save(order));
    }

    public Order create(OrderDto orderDto, User user) {
        LocalDate arrival = UtilService.getDate(orderDto.getArrivalString());
        LocalDate departure = UtilService.getDate(orderDto.getDepartureString());
        return buildOrder(orderDto.getNumber(), arrival, departure, user, Role.ROLE_USER);
    }

    @Transactional
    public Order saveOrderIfEmpty(Order order) {
        if (orderRepo.checkRoomByRoomId(order.getRoom().getId(), Date.valueOf(order.getArrival()),
                Date.valueOf(order.getDeparture())) > 0) {
            throw new NotEmptyRoomException("This room is occupied during this period!");
        }
        return orderRepo.save(order);
    }

    @Transactional
    public void setCancelInvoice() {
        LocalDateTime canceledDate = LocalDateTime.now().minusDays(2);
        orderRepo.updateInvoiceStatusByOldRegistered(Timestamp.valueOf(canceledDate));
        orderRepo.updateAllOrderStatusIfInvoiceCancel();
    }

    public Order saveOrderProposal(OrderDto dto, User user) {
        LocalDate arrival = UtilService.getDate(dto.getArrivalString());
        LocalDate departure = UtilService.getDate(dto.getDepartureString());
        Order order = new Order(arrival, departure, dto.getGuests(), dto.getCategory(), LocalDateTime.now(), 0);
        order.setStatus(Order.Status.NEW);
        order.setUser(user);
        return save(order);
    }

    private Order buildOrder(int roomNumber, LocalDate arrival, LocalDate departure, User user, Role role) {
        Room room = roomService.getByNumber(roomNumber);
        Order tempOrder = new Order(arrival, departure, room.getGuests(), room.getCategory(), LocalDateTime.now(),
                room.getPrice());
        tempOrder.setRoom(room);
        tempOrder.setUser(user);
        user.addOrder(tempOrder);
        tempOrder.setStatus(role == Role.ROLE_MANAGER ? Order.Status.BOOKED : Order.Status.CONFIRMED);
        return tempOrder;
    }

    public OrderDto createOrderDto(HttpServletRequest request, Room room) {
        HttpSession session = request.getSession();
        LocalDate start = session.getAttribute("arrival") == null ? LocalDate.now()
                : (LocalDate) session.getAttribute("arrival");
        LocalDate end = session.getAttribute("departure") == null ? LocalDate.now()
                : (LocalDate) session.getAttribute("departure");
        end = end.isEqual(start) ? start.plusDays(1) : end;
        start = start.isAfter(end) ? end.minusDays(1) : start;
        return new OrderDto(room.getNumber(), room.getCategory(), room.getGuests(), room.getDescription(),
                start, end, room.getPrice(), room.getImgName());
    }
}
