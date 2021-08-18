package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.exception.AlreadyException;
import org.aleks4ay.hotel.model.Role;
import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.repository.UserRepo;
import org.aleks4ay.hotel.utils.Encrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        User user = new User(login, firstName, lastName, pass , true, LocalDateTime.now(), 0d);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(Role.ROLE_USER);
        return Optional.of(userRepo.save(user));
    }

    public boolean checkLogin(String login) {
        return userRepo.findByLogin(login).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByLogin(login);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return userOptional.get();
    }
}
