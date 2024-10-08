package dev.patika.VetManagementSystem.dto.request.customer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSaveRequest {//Customer kayıt etmek için gerekli verileri tutar
    @NotNull(message = "Müşteri Adı boş olamaz")
    private String name;
    @NotNull(message = "Müşteri iletişim bilgisi boş olamaz")
    private String phone;
    @NotNull(message = "Müşteri e-posta adresi boş olamaz")
    private String mail;
    @NotNull(message = "Müşteri adres bilgisi boş olamaz")
    private String address;
    @NotNull(message = "Müşteri şehir bilgisi boş olamaz")
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
