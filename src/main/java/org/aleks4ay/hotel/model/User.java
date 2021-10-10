package org.aleks4ay.hotel.model;

import org.aleks4ay.hotel.exception.NoMoneyException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Entity
@Table(name = "usr")
public class User implements UserDetails{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @Size(min = 4, max = 100)
    @Pattern(regexp = "^[_A-Za-z0-9.]+$",
            message="{validation.login.Characters.message}")
//            message="Login can contain these characters: letters, numbers, point and underscore")
    private String login;

    private String name;
    private String surname;
    private String password;
    private boolean active = false;
    private LocalDateTime registered = LocalDateTime.now();

    @Column(name = "bill", precision = 12, scale = 2)
    private BigDecimal bill;

    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(targetClass = Role.class , fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();


    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(String login, String name, String surname, String password, boolean active, LocalDateTime registered,
                double money) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.active = active;
        this.registered = registered;
        setBill(money);
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        }
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
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

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bigDecimal) {
        bill = bigDecimal;
        bill = bill.setScale(2, BigDecimal.ROUND_FLOOR);
    }

    public void setBill(double money) {
        bill = new BigDecimal(Double.toString(money));
        bill = bill.setScale(2, BigDecimal.ROUND_FLOOR);
    }

    public void addBill(double money) {
        if (bill == null) {
            bill = new BigDecimal(Double.toString(money));
            bill = bill.setScale(2, BigDecimal.ROUND_FLOOR);
        } else {
            bill = bill.add(new BigDecimal(Double.toString(money)));
        }
    }

    public void reduceBill(double money) {
        if (bill.doubleValue() > money) {
            BigDecimal reducedMoney = new BigDecimal(Double.toString(money));
            bill = bill.subtract(reducedMoney);
        } else {
            throw new NoMoneyException("Sorry, there are not enough funds in your account");
        }
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
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
        return roles.contains(Role.ROLE_ADMIN);
    }

    public boolean isManager() {
        return roles.contains(Role.ROLE_MANAGER);
    }

    public boolean isClient() {
        return roles.contains(Role.ROLE_USER);
    }

    public String getRegisteredStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return registered.format(formatter);
    }

    public Role getMainRole() {
        return isAdmin() ? Role.ROLE_ADMIN
                : isManager() ? Role.ROLE_MANAGER
                : isClient() ? Role.ROLE_USER
                : Role.ROLE_GUEST;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", bill=" + bill +
                ", active=" + active +
                ", registered=" + registered +
                ", role=" + roles +
                ", orders=" + orders +
                '}';
    }

}
