package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Order;
import org.aleks4ay.hotel.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {
    List<Order> findAllByUser(User user);

}
