package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.exception.AlreadyException;
import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.repository.UserRepo;
import org.aleks4ay.hotel.utils.Encrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserService {
    private UserRepo userRepo;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    private static final Logger log = LogManager.getLogger(UserService.class);

    public Optional<User> getById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<User> getByLogin(String login) {
        return userRepo.findByLogin(login);
    }

    public List<User> getAll() {
        return (List<User>) userRepo.findAll();
    }

    public Map<Long, User> getAllAsMap() {
        List<User> users = getAll();
        Map<Long, User> userMap = new HashMap<>();
        for (User u : users) {
            userMap.put(u.getId(), u);
        }
        return userMap;
    }


    @Transactional
    public Optional<User> create(User userFromView) {
        String login = userFromView.getLogin();
        String firstName = userFromView.getName();
        String lastName = userFromView.getSurname();
        String pass = userFromView.getPassword();
        if (checkLogin(login)) {
            throw new AlreadyException("User with login '" + login + "' already exists");
        }
        String encryptPassword = Encrypt.hash(pass, "SHA-256");
        User user = new User(login, firstName, lastName, encryptPassword, true, LocalDateTime.now(), 0d, User.Role.ROLE_USER);
        return Optional.of(userRepo.save(user));
    }

    public boolean checkLogin(String login) {
//        final Optional<User> userOptional = userRepo.findByLogin(login);
//        System.out.println("userOptional=" + userOptional);
//        System.out.println("userOptional.isPresent()=" + userOptional.isPresent());
        return userRepo.findByLogin(login).isPresent();
    }

    public boolean checkPassword(String login, String pass) {
        final Optional<User> userFromDB = userRepo.findByLogin(login);
        if (userFromDB.isPresent()) {
            String passFromDb = userFromDB.get().getPassword();
            String encryptedPassword = Encrypt.hash(pass, "SHA-256");
            return passFromDb.equals(encryptedPassword);
        }
        return false;
    }
}
