package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Schedule;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepo extends CrudRepository<Schedule, Long> {

    @Override
    Schedule save(Schedule schedule);


    @Query(value = "select count(t) from timetable t where t.room_id = ?1 and ((t.arrival between ?2 and ?3) " +
            "or (t.departure between ?4 and ?5))", nativeQuery = true)
    int findAllByRoomId(Long roomId, Date start1, Date end1, Date start2, Date end2);

    List<Schedule> findAllByRoomId (long roomId);
}
