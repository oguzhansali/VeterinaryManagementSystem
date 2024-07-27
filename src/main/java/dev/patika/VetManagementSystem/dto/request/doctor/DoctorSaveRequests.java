package dev.patika.VetManagementSystem.dto.request.doctor;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSaveRequests {//Doctor kayıt etmek için gerekli verileri tutar
    @NotNull(message = "Doctor Adı boş olamaz")
    private String name;
    @NotNull(message = "Doktor iletişim bilgisi boş olamaz")
    private String phone;
    @NotNull(message = "Doktor e-posta adresi boş olamaz")
    private String mail;
    @NotNull(message = "Doktor adres bilgisi boş olamaz")
    private String address;
    @NotNull(message = "Doktor şehir bilgisi boş olamaz")
    private String city;

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
}
