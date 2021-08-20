package org.aleks4ay.hotel.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
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
    private String login;
    private String name;
    private String surname;
    private String password;
    private boolean active = false;
    private LocalDateTime registered = LocalDateTime.now();
    private double bill;

    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(targetClass = Role.class , fetch = FetchType.EAGER) //    @Fetch(FetchMode.SUBSELECT)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Proposal> proposals = new ArrayList<>();



    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(String login, String name, String surname, String password, boolean active, LocalDateTime registered,
                double bill) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.active = active;
        this.registered = registered;
        this.bill = bill;
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

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
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

    public List<Proposal> getProposals() {
        return proposals;
    }

    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }

    public void addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setUser(this);
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

//    public String getMainRoleAsString() {
//        return getMainRole().toString();
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", active=" + active +
                ", registered=" + registered +
                ", role=" + roles +
//                ", orders=" + orders +
                '}';
    }

}
