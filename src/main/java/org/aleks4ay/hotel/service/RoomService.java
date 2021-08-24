package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.exception.AlreadyException;
import org.aleks4ay.hotel.exception.NotFoundException;
import org.aleks4ay.hotel.model.*;
import org.aleks4ay.hotel.repository.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
public class RoomService {

    private final RoomRepo roomRepo;

    @Autowired
    public RoomService(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    public Room getById(Long id) {
        return roomRepo.findById(id).orElseThrow(() -> new NotFoundException("Room with id=" + id + "not found"));
    }

    public Room getByNumber(int number) {
        return roomRepo.findByNumber(number).orElseThrow(() -> new NotFoundException("room #" + number + " not found"));
    }


    public Room save(Room room) {
        try {
            return roomRepo.save(room);
        } catch (Exception e) {
            throw new AlreadyException("Room with number #" + room.getNumber() + "already exists");
        }
    }

    public Page<Room> findAll(int numPage, int rowsOnPage, String sortMethod) {
        Pageable sortedPage = PageRequest.of(numPage, rowsOnPage, Sort.by(sortMethod));
        return roomRepo.findAll(sortedPage);
    }

    public List<Room> getRoomsWithFilter(HttpServletRequest request) {
        Object categoryObj = request.getSession().getAttribute("category");
        Object guestsObj = request.getSession().getAttribute("guests");
        Object arrivalObj = request.getSession().getAttribute("arrival");
        Object departureObj = request.getSession().getAttribute("departure");
        List<Room> rooms;
        if (arrivalObj == null && departureObj == null) {
            rooms = roomRepo.findAll();
        } else if (departureObj == null) {
            rooms = roomRepo.findEmptyRoom(Date.valueOf((LocalDate)arrivalObj));
        } else if (arrivalObj == null) {
            rooms = roomRepo.findEmptyRoom(Date.valueOf((LocalDate)departureObj));
        } else {
            rooms = roomRepo.findEmptyRoom(Date.valueOf((LocalDate) arrivalObj), Date.valueOf((LocalDate) departureObj));
        }

        if (categoryObj != null) {
            rooms = rooms.stream()
                    .filter(room -> room.getCategory() == categoryObj)
                    .collect(Collectors.toList());
        }
        if (guestsObj != null) {
            rooms = rooms.stream()
                    .filter(room -> room.getGuests() == (int) guestsObj)
                    .collect(Collectors.toList());
        }
        return rooms;
    }

    public List<Room> doPagination(int positionOnPage, int page, List<Room> entities) {
        return new UtilService<Room>().doPagination(positionOnPage, page, entities);
    }


    public List<Room> setSorting(List<Room> rooms, String sortMethod) {
        if (sortMethod.equalsIgnoreCase("category")) {
            rooms.sort(comparing(Room::getCategory));
        } else if (sortMethod.equalsIgnoreCase("price")) {
            rooms.sort(comparing(Room::getPrice));
        } else if (sortMethod.equalsIgnoreCase("guests")) {
            rooms.sort(comparing(Room::getGuests));
        } else if (sortMethod.equalsIgnoreCase("number")) {
            rooms.sort(comparing(Room::getNumber));
        } else {
            rooms.sort(comparing(Room::getId));
        }
        return rooms;
    }

    public void addOldValues(Map<String, Object> model, Room room) {
        model.put("roomExistMessage", "Room with this number exists!");
        model.put("oldNumber", room.getNumber());
        model.put("oldDescription", room.getDescription());
        model.put("oldGuests", room.getGuests());
        model.put("oldPrice", room.getPrice());
        model.put("oldCategory", room.getCategory());
    }
}
