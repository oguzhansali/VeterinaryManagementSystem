package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Integer> {
    Optional<Doctor> findByMail(String mail);

    @Query("SELECT DISTINCT d FROM Doctor d JOIN d.appointments a WHERE d.id = :doctorId AND a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Doctor> findDoctorsByIdAndDateRange(@Param("doctorId") int doctorId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);



}
