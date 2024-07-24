package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepo extends JpaRepository<Animal,Integer> {
    void deleteByCustomerId(int customerId);
}
