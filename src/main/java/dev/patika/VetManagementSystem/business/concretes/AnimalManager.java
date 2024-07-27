package dev.patika.VetManagementSystem.business.concretes;

import dev.patika.VetManagementSystem.business.abtracts.IAnimalService;
import dev.patika.VetManagementSystem.business.abtracts.IAppointmentService;
import dev.patika.VetManagementSystem.core.config.modelMapper.IModelMapperService;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.AnimalRepo;
import dev.patika.VetManagementSystem.dao.AppointmetRepo;
import dev.patika.VetManagementSystem.dao.CustomerRepo;
import dev.patika.VetManagementSystem.dao.VaccineRepo;
import dev.patika.VetManagementSystem.dto.response.animal.AnimalResponse;
import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnimalManager implements IAnimalService {
    private final AnimalRepo animalRepo;
    private final VaccineRepo vaccineRepo;
    private final CustomerRepo customerRepo;
    private final AppointmetRepo appointmetRepo;
    private final IAppointmentService appointmentService;
    private final IModelMapperService modelMapper;

    public AnimalManager(AnimalRepo animalRepo, VaccineRepo vaccineRepo, CustomerRepo customerRepo, AppointmetRepo appointmetRepo, IAppointmentService appointmentService, IModelMapperService modelMapper) {
        this.animalRepo = animalRepo;
        this.vaccineRepo = vaccineRepo;
        this.customerRepo = customerRepo;
        this.appointmetRepo = appointmetRepo;
        this.appointmentService = appointmentService;
        this.modelMapper = modelMapper;
    }

    // Yeni bir hayvan kaydeder
    @Override
    public Animal save(Animal animal) {
        // Customer nesnesini kontrol eder
        if (animal.getCustomer() == null || !customerRepo.existsById(animal.getCustomer().getId())) {
            throw new NotFoundException(Msg.NOT_FOUND);
        }
        return animalRepo.save(animal);
    }

    // Belirli bir hayvanın bilgilerini alır
    @Override
    public Animal get(int id) {
        return animalRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    // Hayvanları sayfalı olarak alır
    @Override
    public Page<Animal> cursor(int page, int pageSie) {
        Pageable pageable = PageRequest.of(page, pageSie);
        return this.animalRepo.findAll(pageable);
    }

    // Belirli bir hayvanın bilgisini günceller
    @Override
    public Animal update(Animal animal) {
        this.get(animal.getId());
        return this.animalRepo.save(animal);
    }

    // Belirli bir hayvanın bilgisini siler
    @Transactional
    @Override
    public boolean delete(int id) {
        Animal animal = animalRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
        // Hayvana ait randevuları ve aşıları siler
        appointmetRepo.deleteByAnimalId(id);
        vaccineRepo.deleteByAnimalId(id);
        // Hayvanı siler
        animalRepo.delete(animal);
        return true;
    }

    // Belirli bir tarih aralığında randevusu olan hayvanları alır
    @Override
    public List<AnimalResponse> getAnimalsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Appointment> appointments = appointmentService.getAppointmentsWithinDateRange(startDate, endDate);

        Set<Animal> animalSet = appointments.stream()
                .map(Appointment::getAnimal)
                .collect(Collectors.toSet());

        List<Animal> animals = new ArrayList<>(animalSet);

        return animals.stream().map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class)).collect(Collectors.toList());
    }

    //Hayvan ismine göre arama yapar
    @Override
    public Animal getByName(String name) {
        return animalRepo.findByName(name).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
    }
}
