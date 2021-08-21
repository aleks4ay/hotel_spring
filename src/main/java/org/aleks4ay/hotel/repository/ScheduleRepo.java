package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Schedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ScheduleRepo extends CrudRepository<Schedule, Long> {

    @Override
    Schedule save(Schedule schedule);


/*    @Query(value = "select count(t) from timetable t where t.room_id = ?1 and ((t.arrival between ?2 and ?3) " +
            "or (t.departure between ?2 and ?3))", nativeQuery = true)
    int findAllByRoomId(Long roomId, Date start1, Date end1);*/


    @Query(value = "select count(t) from timetable t where t.room_id = ?1 and ((t.arrival between ?2 and ?3) or " +
            "(t.departure between ?2 and ?3) or(?2 between t.arrival and t.departure))", nativeQuery = true)
    int findOccupiedSchedule(Long roomId, Date start1, Date end1/*, Date start2, Date end2*/);


    @Query(value = "select count(t) from timetable t where t.room_id = ?1 and ?2 between t.arrival and t.departure",
            nativeQuery = true)
    int checkRoomByDate(Long roomId, Date checkedDate);

    List<Schedule> findAllByRoomId (long roomId);
}
