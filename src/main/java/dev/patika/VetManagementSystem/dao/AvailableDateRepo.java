package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableDateRepo extends JpaRepository<AvailableDate,Integer> {
    void deleteByDoctorId(int doctorId);
}
