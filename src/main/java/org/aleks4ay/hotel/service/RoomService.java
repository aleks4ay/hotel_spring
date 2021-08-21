package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.exception.NotFoundException;
import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Room;
import org.aleks4ay.hotel.model.Schedule;
import org.aleks4ay.hotel.repository.RoomRepo;
import org.aleks4ay.hotel.repository.ScheduleRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

import static java.util.Comparator.comparing;

@Service
@Transactional(readOnly = true)
public class RoomService {
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private ScheduleRepo scheduleRepo;

    private static final Logger log = LogManager.getLogger(RoomService.class);

    public Optional<Room> getById(Long id) {
        return roomRepo.findById(id);
    }

    public Optional<Room> getByNumber(int number) {
        return roomRepo.findByNumber(number);
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
        Object categoryObj = request.getSession().getAttribute("category");
        Object guestsObj = request.getSession().getAttribute("guests");
        Object arrivalObj = request.getSession().getAttribute("arrival");
        Object departureObj = request.getSession().getAttribute("departure");

        if (categoryObj != null) {
            Category category = (Category)categoryObj;
            if (guestsObj != null) {
                int guests = (int) guestsObj;
                roomList = getAllWithFilters(category, guests);
            } else {
                roomList = getAllWithFilters(category);
            }
        } else if (guestsObj != null) {
            int guests = (int) guestsObj;
            roomList = getAllWithFilters(guests);
        } else {
            roomList = getAll();
        }
        if (arrivalObj != null && departureObj == null) {
            roomList = getRoomsWithDate((LocalDate)arrivalObj, roomList);
        }
        if (arrivalObj == null && departureObj != null) {
            roomList = getRoomsWithDate((LocalDate)departureObj, roomList);
        }
        if (arrivalObj != null && departureObj != null) {
            roomList = getRoomsWithInterval((LocalDate)arrivalObj, (LocalDate)departureObj, roomList);
        }
        return roomList;
    }

    public List<Room> getRoomsWithDate(LocalDate localDate, List<Room> rooms) {
        return getRoomsWithInterval(localDate, localDate, rooms);
    }

    public List<Room> getRoomsWithInterval(LocalDate arrival, LocalDate departure, List<Room> rooms) {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            boolean isEmpty = true;
            for (Schedule s : room.getSchedules()) {
                boolean isScheduleAfter = arrival.isBefore(s.getArrival()) && departure.isBefore(s.getArrival());
                boolean isScheduleBefore = arrival.isAfter(s.getDeparture()) && departure.isAfter(s.getDeparture());
                if ( !(isScheduleAfter || isScheduleBefore) ) {
                    isEmpty = false;
                    log.info("Room #{} is occupied from {} to {} and not might be able to booked from {} to {}",
                            room.getNumber(), s.getArrival(), s.getDeparture(), arrival, departure);
                }
            }
            if (isEmpty) {
                result.add(room);
            }
        }
        return result;
    }

    public List<Room> doPagination(int positionOnPage, int page, List<Room> entities) {
        return new UtilService<Room>().doPagination(positionOnPage, page, entities);
    }


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

    private List<Room> getAllWithFilters(Category category, int guests) {
        return roomRepo.findAllByCategoryAndGuests(category, guests);
    }

    private List<Room> getAllWithFilters(Category category) {
        return roomRepo.findAllByCategory(category);
    }

    private List<Room> getAllWithFilters(int guests) {
        return roomRepo.findAllByGuests(guests);
    }


    public List<Room> setSorting(List<Room> rooms, HttpServletRequest request) {
        String sortMethod = (String) request.getSession().getAttribute("sortMethod");
        if (sortMethod.equalsIgnoreCase("byCategory")) {
            rooms.sort(comparing(Room::getCategory));
        } else if (sortMethod.equalsIgnoreCase("byPrice")) {
            rooms.sort(comparing(Room::getPrice));
        } else if (sortMethod.equalsIgnoreCase("byGuests")) {
            rooms.sort(comparing(Room::getGuests));
        } else {
            rooms.sort(comparing(Room::getNumber));
        }
        return rooms;
    }
}
