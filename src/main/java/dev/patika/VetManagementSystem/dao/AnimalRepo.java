package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.Appointment;
import dev.patika.VetManagementSystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepo extends JpaRepository<Animal,Integer> {
    void deleteByCustomerId(int customerId);
    List<Animal> findByCustomerId(Integer customerId);
    Optional<Animal> findByName(String name);




}
