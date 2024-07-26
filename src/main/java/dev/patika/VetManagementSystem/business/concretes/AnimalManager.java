package dev.patika.VetManagementSystem.business.concretes;

import dev.patika.VetManagementSystem.business.abtracts.IAnimalService;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.AnimalRepo;
import dev.patika.VetManagementSystem.dao.AppointmetRepo;
import dev.patika.VetManagementSystem.dao.CustomerRepo;
import dev.patika.VetManagementSystem.dao.VaccineRepo;
import dev.patika.VetManagementSystem.entity.Animal;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AnimalManager implements IAnimalService {
    private final AnimalRepo animalRepo;
    private final VaccineRepo vaccineRepo;
    private final CustomerRepo customerRepo;
    private final AppointmetRepo appointmetRepo;

    public AnimalManager(AnimalRepo animalRepo,VaccineRepo vaccineRepo,CustomerRepo customerRepo,AppointmetRepo appointmetRepo) {
        this.animalRepo = animalRepo;
        this.vaccineRepo= vaccineRepo;
        this.customerRepo=customerRepo;
        this.appointmetRepo=appointmetRepo;
    }

    @Override
    public Animal save(Animal animal) {
        // Customer nesnesini kontrol eder
        if (animal.getCustomer() == null || !customerRepo.existsById(animal.getCustomer().getId())) {
            throw new NotFoundException(Msg.NOT_FOUND);
        }
        return animalRepo.save(animal);
    }

    @Override
    public Animal get(int id) {
        return animalRepo.findById(id).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Animal> cursor(int page, int pageSie) {
        Pageable pageable = PageRequest.of(page,pageSie);
        return this.animalRepo.findAll(pageable);
    }

    @Override
    public Animal update(Animal animal) {
        this.get(animal.getId());
        return this.animalRepo.save(animal);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        Animal animal = animalRepo.findById(id)
                .orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
        appointmetRepo.deleteByAnimalId(id);
        vaccineRepo.deleteByAnimalId(id);
        animalRepo.delete(animal);
        return true;
    }
}
