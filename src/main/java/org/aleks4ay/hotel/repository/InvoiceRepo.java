package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepo extends CrudRepository<Invoice, Long> {

    Page<Invoice> findAll(Pageable pageable);

    @Query(value = "select i.* from order_invoice x INNER JOIN invoice i on x.invoice_id = i.id AND x.order_id = ?1",
            nativeQuery = true)
    Invoice findByOrderId(long orderId);
}
