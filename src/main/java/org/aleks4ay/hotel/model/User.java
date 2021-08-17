package org.aleks4ay.hotel.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String login;
    private String name;
    private String surname;
    private String password;
    private boolean active = false;
    private LocalDateTime registered = LocalDateTime.now();
    private double bill;
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(String login, String name, String surname, String password, boolean active, LocalDateTime registered,
                double bill, Role role) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.active = active;
        this.registered = registered;
        this.bill = bill;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
//        return Encrypt.hash(password, "SHA-256");
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }



    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        for (Order o : orders) {
            addOrder(o);
        }
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.setUser(this);
    }

    public boolean isAdmin() {
        return role.equals(Role.ROLE_ADMIN);
    }

    public boolean isManager() {
        return role.equals(Role.ROLE_MANAGER);
    }

    public boolean isClient() {
        return role.equals(Role.ROLE_USER);
    }

    public String getDateByPattern() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return registered.format(formatter);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", active=" + active +
                ", registered=" + registered +
                ", role=" + role +
//                ", orders=" + orders +
                '}';
    }

    public enum Role {
        ROLE_GUEST,
        ROLE_USER,
        ROLE_MANAGER,
        ROLE_ADMIN;

        public String getTitle() {
            return this.toString().replace("ROLE_", "");
        }
    }
}
