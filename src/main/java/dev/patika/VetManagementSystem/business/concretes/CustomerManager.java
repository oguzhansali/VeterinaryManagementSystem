package dev.patika.VetManagementSystem.business.concretes;

import dev.patika.VetManagementSystem.business.abtracts.ICustomerService;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.AnimalRepo;
import dev.patika.VetManagementSystem.dao.CustomerRepo;
import dev.patika.VetManagementSystem.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerManager implements ICustomerService {

    private final CustomerRepo customerRepo;
    private  final AnimalRepo animalRepo;

    public CustomerManager(CustomerRepo customerRepo,AnimalRepo animalRepo) {
        this.customerRepo = customerRepo;
        this.animalRepo=animalRepo;
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public Customer get(int id) {
        return customerRepo.findById(id).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Customer> cursor(int page, int pageSie) {
        Pageable pageable = PageRequest.of(page,pageSie);
        return this.customerRepo.findAll(pageable);
    }

    @Override
    public Customer update(Customer customer) {
        this.get(customer.getId());
        return this.customerRepo.save(customer);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        Customer customer= customerRepo.findById(id)
                .orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));

        this.animalRepo.deleteByCustomerId(id);
        this.customerRepo.delete(customer);
        return true;
    }
}
