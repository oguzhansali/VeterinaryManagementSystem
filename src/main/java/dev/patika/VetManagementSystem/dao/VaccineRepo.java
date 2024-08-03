package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.Appointment;
import dev.patika.VetManagementSystem.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine,Integer> {
    void deleteByAnimalAid(int animalId);
    List<Vaccine> findByAnimalAid(Integer animalId);
    List<Vaccine> findByProtectionFnshDateBetween(LocalDate startDate, LocalDate endDate);

    /*@Query("SELECT v FROM Vaccine v JOIN FETCH v.animal WHERE v.protecFnshDate = :protectionFnshDate AND v.protecFnshDate BETWEEN :startDate AND :endDate")
    List<Vaccine> findVaccinesByProtectionFnshDateAndDateRange(@Param("protectionFnshDate") LocalDate protectionFnshDate,
                                                              @Param("startDate") LocalDate startDate,
                                                              @Param("endDate") LocalDate endDate);
*/

}
