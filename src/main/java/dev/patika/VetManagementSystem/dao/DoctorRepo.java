package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Integer> {
    Optional<Doctor> findByMail(String mail);

}
