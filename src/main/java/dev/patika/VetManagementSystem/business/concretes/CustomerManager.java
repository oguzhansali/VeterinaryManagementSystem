package dev.patika.VetManagementSystem.business.concretes;

import dev.patika.VetManagementSystem.business.abtracts.ICustomerService;
import dev.patika.VetManagementSystem.core.exception.EmailAlreadyExistsException;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.AnimalRepo;
import dev.patika.VetManagementSystem.dao.AppointmetRepo;
import dev.patika.VetManagementSystem.dao.CustomerRepo;
import dev.patika.VetManagementSystem.dao.VaccineRepo;
import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.Customer;
import dev.patika.VetManagementSystem.entity.Doctor;
import dev.patika.VetManagementSystem.entity.Vaccine;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerManager implements ICustomerService {

    private final CustomerRepo customerRepo;
    private  final AnimalRepo animalRepo;
    private final VaccineRepo vaccineRepo;
    private final AppointmetRepo appointmetRepo;

    public CustomerManager(CustomerRepo customerRepo,AnimalRepo animalRepo,VaccineRepo vaccineRepo,AppointmetRepo appointmetRepo) {
        this.customerRepo = customerRepo;
        this.animalRepo=animalRepo;
        this.vaccineRepo=vaccineRepo;
        this.appointmetRepo=appointmetRepo;
    }

    @Override
    public Customer save(Customer customer) {
        Optional<Customer> existingCustomerByMail = customerRepo.findByMail(customer.getMail());
        if (existingCustomerByMail.isPresent()) {
            throw new EmailAlreadyExistsException(Msg.ALREADY_CREATEED);
        }
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

        // Customer ile ilişkili Animal'ları bul
        List<Animal> animals = animalRepo.findByCustomerId(customer.getId());

        // Her Animal için ilişkili Vaccine'leri sil
        for (Animal animal : animals) {
            List<Vaccine> vaccines = vaccineRepo.findByAnimalId(animal.getId());
            if (vaccines != null && !vaccines.isEmpty()) {
                vaccineRepo.deleteAll(vaccines);
            }

        }

        appointmetRepo.deleteByCustomerId(id);
        //Tüm Animal'ları sil
        animalRepo.deleteAll(animals);

        // Customer'ı sil
        customerRepo.delete(customer);

        return true;
    }
}
