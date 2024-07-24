package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine,Integer> {
}
