package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.model.Room;
import org.aleks4ay.hotel.model.Schedule;
import org.aleks4ay.hotel.repository.ScheduleRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ScheduleService{

    private ScheduleRepo scheduleRepo;
    private static final Logger log = LogManager.getLogger(ScheduleService.class);

    @Autowired
    public void setScheduleRepo(ScheduleRepo scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    public Optional<Schedule> getById(long id) {
        return scheduleRepo.findById(id);
    }

    public List<Schedule> getAll() {
        return (List<Schedule>) scheduleRepo.findAll();
    }

    public Map<Long, Schedule> getAllAsMap() {
        List<Schedule> schedules = getAll();
        Map<Long, Schedule> scheduleMap = new HashMap<>();
        for (Schedule o : schedules) {
            scheduleMap.put(o.getId(), o);
        }
        return scheduleMap;
    }



    public Map<Long, List<Schedule>> getAllAsMapByRoomId() {
        List<Schedule> schedules = getAll();
        Map<Long, List<Schedule>> scheduleMap = new HashMap<>();
        for (Schedule item : schedules) {
            long roomId = item.getRoom().getId();
            if (!scheduleMap.containsKey(roomId)) {
                List<Schedule> newList = new ArrayList<>();
                newList.add(item);
                scheduleMap.put(roomId, newList);
            } else {
                scheduleMap.get(roomId).add(item);
            }
        }
        return scheduleMap;
    }


    public boolean checkRoom(Schedule schedule) {
        final Integer result = scheduleRepo.findAllByRoomId(
                schedule.getRoom().getId(),
                java.sql.Date.valueOf(schedule.getArrival()),
                java.sql.Date.valueOf(schedule.getDeparture()),
                java.sql.Date.valueOf(schedule.getArrival()),
                java.sql.Date.valueOf(schedule.getDeparture()));
        if (result == null || result == 0) {
            System.out.println("************* N U L L *****************");
            return true;
        }
        if (result > 0) {
            log.info("Selected room #{} already occupied.", schedule.getRoom().getNumber());
            return false;
        }
        return true;
    }

    @Transactional
    public Schedule create(Schedule schedule) {
//        System.out.println(schedule);
//        final Optional<Room> room = new RoomService().getById(20L);
//        schedule.setRoom(room.get());
//        Schedule schedule1 = scheduleRepo.save(schedule);
//        System.out.println("schedule1 = " + schedule1);
        return scheduleRepo.save(schedule);
    }

    @Transactional
    public Schedule updateStatus(Schedule schedule) {
        return scheduleRepo.save(schedule);
    }

    public List<Schedule> getScheduleByRoomId(long roomId) {
        return scheduleRepo.findAllByRoomId(roomId);
    }
}
