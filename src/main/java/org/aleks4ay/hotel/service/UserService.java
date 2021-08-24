package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.exception.AlreadyException;
import org.aleks4ay.hotel.exception.NotFoundException;
import org.aleks4ay.hotel.model.Role;
import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService{

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User with id = " + id + " non found"));
    }

    public User getByLogin(String login) {
        return userRepo.findByLogin(login).orElseThrow(() -> new NotFoundException("User '" + login + "' non found"));
    }

    public Page<User> findAll(int numPage, int rowsOnPage, String sortMethod) {
        Pageable sortedPage = PageRequest.of(numPage, rowsOnPage, Sort.by(sortMethod));
        return userRepo.findAll(sortedPage);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(Role.ROLE_USER);
        user.addBill(0d);
        try {
            return userRepo.save(user);
        } catch (Exception e) {
            throw new AlreadyException("User with login '" + user.getLogin() + "' already exists");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepo.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User '" + login + "' non found"));
    }

    public User update(User user) {
        return userRepo.save(user);
    }

    public void addOldValues(Map<String, Object> model, User user) {
        model.put("wrongLogin", "User exists!");
        model.put("oldLogin", user.getLogin());
        model.put("oldFirstName", user.getName());
        model.put("oldLastName", user.getSurname());
    }
}
