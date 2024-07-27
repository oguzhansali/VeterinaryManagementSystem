package dev.patika.VetManagementSystem.business.abtracts;

import dev.patika.VetManagementSystem.entity.Customer;
import dev.patika.VetManagementSystem.entity.Doctor;
import org.springframework.data.domain.Page;

public interface ICustomerService {
    Customer save(Customer customer);
    Customer get(int id);
    Page<Customer> cursor(int page, int pageSie);
    Customer update(Customer customer);
    boolean delete(int id);
    Customer getByName(String name);
}
