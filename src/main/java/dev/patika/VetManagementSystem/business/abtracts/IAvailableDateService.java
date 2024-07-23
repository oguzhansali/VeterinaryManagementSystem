package dev.patika.VetManagementSystem.business.abtracts;

import dev.patika.VetManagementSystem.entity.AvailableDate;
import dev.patika.VetManagementSystem.entity.Doctor;
import org.springframework.data.domain.Page;

public interface IAvailableDateService {
    AvailableDate save(AvailableDate availableDate);
    AvailableDate get(int id);
    Page<AvailableDate> cursor(int page, int pageSie);
    AvailableDate update(AvailableDate availableDate);
    boolean delete(int id);


}
