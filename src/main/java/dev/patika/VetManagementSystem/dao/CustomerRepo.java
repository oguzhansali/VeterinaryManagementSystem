package dev.patika.VetManagementSystem.dao;

import dev.patika.VetManagementSystem.entity.Customer;
import dev.patika.VetManagementSystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByMail(String mail);
    Optional<Customer> findByName(String name);
}
