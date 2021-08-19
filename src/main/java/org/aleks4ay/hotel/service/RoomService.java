package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.exception.NotFoundException;
import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Room;
import org.aleks4ay.hotel.repository.RoomRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.Comparator.comparing;

@Service
@Transactional(readOnly = true)
public class RoomService {
    @Autowired
    private RoomRepo roomRepo;

    private static final Logger log = LogManager.getLogger(RoomService.class);

    public Optional<Room> getById(Long id) {
        return roomRepo.findById(id);
    }

    public List<Room> getAll() {
        return (List<Room>) roomRepo.findAll();
    }

    public Map<Long, Room> getAllAsMap() {
        List<Room> rooms = getAll();
        Map<Long, Room> roomMap = new HashMap<>();
        for (Room r : rooms) {
            roomMap.put(r.getId(), r);
        }
        return roomMap;
    }

    public List<Room> getRooms(HttpServletRequest request) {
        List<Room> roomList;
        Object categoryObj = request.getAttribute("category");
        Object guestsObj = request.getAttribute("guests");

        if (categoryObj != null) {
            String categoryString = (String) request.getAttribute("category");
            if (guestsObj != null) {
                int guests = Integer.parseInt((String) request.getAttribute("guests"));
                roomList = getAllWithFilters(Category.valueOf(categoryString), guests);
            } else {
                roomList = getAllWithFilters(Category.valueOf(categoryString));
            }
        } else if (guestsObj != null) {
            int guests = Integer.parseInt((String) request.getAttribute("guests"));
            roomList = getAllWithFilters(guests);
        } else {
            roomList = getAll();
        }
        return roomList;
    }

    public List<Room> doPagination(int positionOnPage, int page, List<Room> entities) {
        UtilService utilService = new UtilService<Room>();
        entities.sort(comparing(Room::getNumber));
        List<Room> result = utilService.doPagination(positionOnPage, page, entities);
        return result;
    }

/*
    public boolean delete(Long id) {
        Connection conn = ConnectionPool.getConnection();
        RoomDao roomDao = new RoomDao(conn);
        boolean result = roomDao.delete(id);
        ConnectionPool.closeConnection(conn);
        return result;
    }*/

    @Transactional
    public Room update(Room room) {
        Optional<Room> optionalRoom = getById(room.getId());
        if (!optionalRoom.isPresent()) {
            throw new NotFoundException("Room with number: " + room.getNumber() + " not found");
        }
        Room roomFromDB = optionalRoom.get();
        roomFromDB.setGuests(room.getGuests());
        roomFromDB.setDescription(room.getDescription());
        roomFromDB.setCategory(room.getCategory());
        roomFromDB.setPrice(room.getPrice());
        return roomRepo.save(roomFromDB);
    }

    public boolean checkNumber(int number) {
        return roomRepo.findByNumber(number).isPresent();
    }

    @Transactional
    public Room save(Room room) {
        return roomRepo.save(room);
    }

    public List<Room> getAllWithFilters(Category category, int guests) {
        return roomRepo.findAllByCategoryAndGuests(category, guests);
    }

    public List<Room> getAllWithFilters(Category category) {
        return roomRepo.findAllByCategory(category);
    }

    public List<Room> getAllWithFilters(int guests) {
        return roomRepo.findAllByGuests(guests);
    }
}
