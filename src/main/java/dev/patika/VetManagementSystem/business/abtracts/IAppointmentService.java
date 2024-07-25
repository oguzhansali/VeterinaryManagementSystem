package dev.patika.VetManagementSystem.business.abtracts;

import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.Appointment;
import org.springframework.data.domain.Page;

public interface IAppointmentService {
    Appointment save(Appointment appointment, int customerId,int animalId,int doctorId);
    Appointment get(int id);
    Page<Appointment> cursor(int page, int pageSie);
    Appointment update(Appointment appointment);
    boolean delete(int id);
}
