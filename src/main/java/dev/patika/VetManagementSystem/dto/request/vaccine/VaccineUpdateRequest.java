package dev.patika.VetManagementSystem.dto.request.vaccine;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VaccineUpdateRequest {//Vaccine güncellemek için gerekli verileri tutar
    private int id;
    private String name;
    private String code;
    private LocalDate protectionStrtDate;
    private LocalDate protectionFnshDate;
    private int animalId;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getProtectionStrtDate() {
        return protectionStrtDate;
    }

    public void setProtectionStrtDate(LocalDate protectionStrtDate) {
        this.protectionStrtDate = protectionStrtDate;
    }

    public LocalDate getProtectionFnshDate() {
        return protectionFnshDate;
    }

    public void setProtectionFnshDate(LocalDate protectionFnshDate) {
        this.protectionFnshDate = protectionFnshDate;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }
}
