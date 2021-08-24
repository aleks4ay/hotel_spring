package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {

    Page<Order> findAllByUserId(Long userId, Pageable sortedPage);

    Page<Order> findAll(Pageable sortedPage);

    @Query(value = "select count(o.*) from order_room x INNER JOIN orders o on x.order_id = o.id AND x.room_id = ?1 " +
            "and ( (?2 BETWEEN o.arrival and o.departure) " +
               "or (?3 BETWEEN o.arrival and o.departure) " +
               "or (o.arrival BETWEEN ?2 and ?3) )", nativeQuery = true)
    int checkRoomByRoomId(Long roomId, Date start1, Date end1);
}

