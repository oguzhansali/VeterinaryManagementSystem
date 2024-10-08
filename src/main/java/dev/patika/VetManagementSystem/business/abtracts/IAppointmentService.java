package dev.patika.VetManagementSystem.business.abtracts;

import dev.patika.VetManagementSystem.dto.response.appointment.AppointmentResponse;
import dev.patika.VetManagementSystem.dto.response.doctor.DoctorResponse;
import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.Appointment;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentService {
    Appointment save(Appointment appointment,int animalId,int doctorId);
    Appointment get(int id);
    Page<Appointment> cursor(int page, int pageSie);
    Appointment update(Appointment appointment);
    boolean delete(int id);
    Optional<Appointment> get(LocalDateTime appointment);
    List<Appointment> getAppointmentsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<AppointmentResponse> findAppointmentsByDoctorIdAndDateRange(int doctorId, LocalDateTime startDate, LocalDateTime endDate);
    List<AppointmentResponse> findAppointmentsByAnimalIdAndDateRange(int animalId, LocalDateTime startDate, LocalDateTime endDate);





}
