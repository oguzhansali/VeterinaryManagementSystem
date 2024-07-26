package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmetRepo extends JpaRepository<Appointment,Integer> {
    Optional<Appointment> findByAppointmentDate(LocalDateTime appoitment);
    List<Appointment> findByAppointmentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    void deleteByDoctorId(int doctorId);
    void deleteByCustomerId(int doctorId);
    void deleteByAnimalId(int doctorId);


}
