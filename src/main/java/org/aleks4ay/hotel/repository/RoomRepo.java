package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {

    Page<Room> findAll(Pageable pageable);
    Optional<Room> findByNumber(int number);


    @Query(value = "select r.* from room r WHERE r.id NOT IN (" +
            "select DISTINCT x.room_id from order_room x INNER JOIN orders o on x.order_id = o.id " +
            "and o.status < 4 " +
            "and (    (?1 BETWEEN o.arrival and o.departure) " +
            "      or (?2 BETWEEN o.arrival and o.departure) " +
            "      or (o.arrival BETWEEN ?1 and ?2))" +
            ")",
            nativeQuery = true)
    List<Room> findEmptyRoom(Date start, Date end);


    @Query(value = "select r.* from room r WHERE r.id NOT IN (" +
            "select DISTINCT x.room_id from order_room x INNER JOIN orders o on x.order_id = o.id " +
            "           and o.status != 4 and o.status != 0 AND " +
            " ?1 BETWEEN o.arrival and o.departure)",
            nativeQuery = true)
    List<Room> findEmptyRoom(Date start);
}
