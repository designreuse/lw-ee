package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;

import javax.persistence.*;

/**
 * @author Nikolay Kuzmenkov.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "password", nullable = false)
    private String userPassword;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @OneToOne(mappedBy = "logiwebDriverAccount")
    private Driver attachedDriver;

    public User() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Role getUserRole() {
        return userRole;
    }

    public Driver getAttachedDriver() {
        return attachedDriver;
    }

    public void setAttachedDriver(Driver attachedDriver) {
        this.attachedDriver = attachedDriver;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }
}
