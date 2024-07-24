package dev.patika.VetManagementSystem.business.abtracts;

import dev.patika.VetManagementSystem.entity.Doctor;
import dev.patika.VetManagementSystem.entity.Vaccine;
import org.springframework.data.domain.Page;

public interface IVaccineService {
    Vaccine save(Vaccine vaccine);
    Vaccine get(int id);
    Page<Vaccine> cursor(int page, int pageSie);
    Vaccine update(Vaccine vaccine);
    boolean delete(int id);
}
