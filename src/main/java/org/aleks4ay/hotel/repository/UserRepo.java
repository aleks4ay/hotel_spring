package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
