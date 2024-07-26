package dev.patika.VetManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "vaccines")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_id")
    private int id;
    @Column(name = "vaccine_name")
    private String name;
    @Column(name = "vaccine_code",unique = true)
    private String code;
    @Column(name = "vaccine_protecStrtDate")
    private LocalDate protectionStrtDate;
    @Column(name = "vaccine_protecFnshDate")
    private LocalDate protectionFnshDate;

    @ManyToOne
    @JoinColumn(name = "vaccine_animal_id",referencedColumnName = "animal_id")
    private Animal animal;

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

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
