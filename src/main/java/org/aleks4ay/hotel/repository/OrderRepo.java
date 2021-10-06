package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    Page<Order> findAllByUserId(Long userId, Pageable sortedPage);

    Page<Order> findAll(Pageable sortedPage);

    @Query(value = "select count(o.*) from order_room x INNER JOIN orders o on x.order_id = o.id AND x.room_id = ?1 " +
            "and o.status < 4 " +
            "and ( (?2 BETWEEN o.arrival and o.departure) " +
               "or (?3 BETWEEN o.arrival and o.departure) " +
               "or (o.arrival BETWEEN ?2 and ?3) )", nativeQuery = true)
    int checkRoomByRoomId(Long roomId, Date start1, Date end1);

    @Modifying
    @Query(value = "update invoice set status = 'CANCEL' where status = 'NEW' and registered < ?1", nativeQuery = true)
    void updateInvoiceStatusByOldRegistered(Timestamp canceledDate);

    @Modifying
    @Query(value = " update orders set status = 4 where status = 2 and id in (" +
            "select x.order_id from order_invoice x inner join invoice i on x.invoice_id = i.id " +
            "and i.status = 'CANCEL');", nativeQuery = true)
    void updateAllOrderStatusIfInvoiceCancel();
}

