package br.com.inventorycontrol.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="customer")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 2,message = "name should have at least 2 characters")
    @Column(nullable = false)
    private String name;

    @Positive
    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    @Email
    private String email;

    @Size(min = 5,message = "name should have at least 5 characters")
    @Column(nullable = false)
    private String username;

    @Size(min = 8,message = "name should have at least 8 characters")
    @Column(nullable = false)
    private String password;

    @Positive
    @Column(nullable = false)
    private int phone;

    
    public User() {
    }

    public User(long id, String name, int age, String email, String username, String password, int phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

        
}
