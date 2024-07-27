package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.Appointment;
import dev.patika.VetManagementSystem.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine,Integer> {
    void deleteByAnimalId(int animalId);
    List<Vaccine> findByAnimalId(Integer animalId);
    List<Vaccine> findByProtectionFnshDateBetween(LocalDate startDate, LocalDate endDate);



}
