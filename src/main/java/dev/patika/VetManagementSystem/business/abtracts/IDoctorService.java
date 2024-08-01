package dev.patika.VetManagementSystem.business.abtracts;

import dev.patika.VetManagementSystem.dto.response.doctor.DoctorResponse;
import dev.patika.VetManagementSystem.entity.Doctor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IDoctorService {
    Doctor save(Doctor doctor);
    Doctor get(int id);
    Page<Doctor> cursor(int page, int pageSie);
    Doctor update(Doctor doctor);
    boolean delete(int id);
    List<DoctorResponse> getDoctorsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate);






}
