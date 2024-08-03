package dev.patika.VetManagementSystem.business.abtracts;

import dev.patika.VetManagementSystem.dto.response.appointment.AppointmentResponse;
import dev.patika.VetManagementSystem.dto.response.vaccine.VaccineResponse;
import dev.patika.VetManagementSystem.entity.Appointment;
import dev.patika.VetManagementSystem.entity.Doctor;
import dev.patika.VetManagementSystem.entity.Vaccine;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IVaccineService {
    Vaccine save(Vaccine vaccine);
    Vaccine get(int id);
    Page<Vaccine> cursor(int page, int pageSie);
    Vaccine update(Vaccine vaccine);
    boolean delete(int id);
    List<Vaccine> findByProtectionFnshDateBetween(LocalDate startDate, LocalDate endDate);
    //List<VaccineResponse> findVaccinesByProtectionFnshDateAndDateRange(LocalDate protectionFnshDate, LocalDate startDate, LocalDate endDate);


}
