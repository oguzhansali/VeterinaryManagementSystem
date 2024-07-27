package dev.patika.VetManagementSystem.business.abtracts;

import dev.patika.VetManagementSystem.dto.response.animal.AnimalResponse;
import dev.patika.VetManagementSystem.dto.response.doctor.DoctorResponse;
import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.Appointment;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IAnimalService {
    Animal save(Animal animal);
    Animal get(int id);
    Page<Animal> cursor(int page, int pageSie);
    Animal update(Animal animal);
    boolean delete(int id);
    List<AnimalResponse> getAnimalsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate);

}
