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


    @Query(value = "select count(t) from timetable t where t.room_id = ?1 and (t.arrival between ?2 and ?3) " +
            "or (t.departure between ?4 and ?5)", nativeQuery = true)
    int findAllByRoomId(Long roomId, Date start1, Date end1, Date start2, Date end2);


/*            @Query(value = "SELECT c FROM Customer c WHERE c.name LIKE '%' || :keyword || '%'"
            + " OR c.email LIKE '%' || :keyword || '%'"
            + " OR c.address LIKE '%' || :keyword || '%'")
    public List<Customer> search(@Param("keyword") String keyword);*/

   /* @Query(value = "SELECT count(t) FROM timetable t WHERE room_id :1? AND " +
            "(arrival BETWEEN ? AND ? OR departure BETWEEN ? AND ?", nativeQuery = true)
    public boolean checkRoom(Schedule schedule);*/

//    Schedule findByRoomIdAndArrivalBetweenAndDepartureBetween (Long roomId, LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2);


    List<Schedule> findAllByRoomId (long roomId);
}
