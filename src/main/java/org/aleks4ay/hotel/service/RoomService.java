package org.aleks4ay.hotel.service;


import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Room;
import org.aleks4ay.hotel.repository.RoomRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class RoomService {
    private RoomRepo roomRepo;
    private static final Logger log = LogManager.getLogger(RoomService.class);

    @Autowired
    public void setOrderRepository(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

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
/*
    public boolean delete(Long id) {
        Connection conn = ConnectionPool.getConnection();
        RoomDao roomDao = new RoomDao(conn);
        boolean result = roomDao.delete(id);
        ConnectionPool.closeConnection(conn);
        return result;
    }*//*

*//*    public Room update(Room room) {
        Connection conn = ConnectionPool.getConnection();
        RoomDao roomDao = new RoomDao(conn);
        room = roomDao.update(room);
        ConnectionPool.closeConnection(conn);
        return room;
    }*/

    @Transactional(readOnly = false)
    public Room create(Room room) {
        return roomRepo.save(room);
    }

    public List<Room> getAllWithFilters(Category category, int guests) {
        return roomRepo.findAllByCategoryAndGuests(category, guests);
    }
}
