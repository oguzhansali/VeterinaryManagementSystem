package dev.patika.VetManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "doctors")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private  int id;
    @NotNull
    @Column(name = "doctor_name")
    private String name;
    @Column(name = "doctor_phone")
    private String phone;
    @Email
    @Column(name = "doctor_mail",unique = true)
    private String mail;
    @Column(name = "doctor_address")
    private String address;
    @Column(name = "doctor_city")
    private String city;

    @OneToMany(mappedBy = "doctor")
    private List<AvailableDate> availableDates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<AvailableDate> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<AvailableDate> availableDates) {
        this.availableDates = availableDates;
    }
}
