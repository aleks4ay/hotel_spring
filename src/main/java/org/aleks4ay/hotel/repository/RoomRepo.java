package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepo extends CrudRepository<Room, Long> {


/*    @Query("select r from room r where r.category = :category and r.guests = :guests")
    List<Room> findAllByCategoryAndGuests(@Param("category") Integer category,
                                   @Param("guests") Integer guests); //, Sort sort*/


    List<Room> findAllByCategoryAndGuests(Category category, Integer guests);

}
