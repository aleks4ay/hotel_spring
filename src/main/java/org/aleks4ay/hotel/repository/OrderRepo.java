package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {

}
