package dev.patika.VetManagementSystem.business.concretes;

import dev.patika.VetManagementSystem.business.abtracts.IAppointmentService;
import dev.patika.VetManagementSystem.core.exception.AppointmentAlreadyExistsException;
import dev.patika.VetManagementSystem.core.exception.DoctorDoesNotAvailableException;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.AnimalRepo;
import dev.patika.VetManagementSystem.dao.AppointmetRepo;
import dev.patika.VetManagementSystem.dao.CustomerRepo;
import dev.patika.VetManagementSystem.dao.DoctorRepo;
import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.Appointment;
import dev.patika.VetManagementSystem.entity.Customer;
import dev.patika.VetManagementSystem.entity.Doctor;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentManager implements IAppointmentService {
    private final AppointmetRepo appointmetRepo;
    private final CustomerRepo customerRepo;
    private final AnimalRepo animalRepo;
    private final DoctorRepo doctorRepo;

    public AppointmentManager(AppointmetRepo appointmetRepo,
                              CustomerRepo customerRepo,
                              AnimalRepo animalRepo,
                              DoctorRepo doctorRepo) {
        this.appointmetRepo = appointmetRepo;
        this.customerRepo = customerRepo;
        this.animalRepo = animalRepo;
        this.doctorRepo = doctorRepo;
    }
    @Transactional
    @Override
    public Appointment save(Appointment appointment, int customerId,int animalId,int doctorId) {
        //Daha önce oluşturulan appointmentleri kontrol eder ve aynı veri girilmesini engeller
        Optional<Appointment> existingAppointmentDate = appointmetRepo.findByAppointmentDate(appointment.getAppointmentDate());
        if (existingAppointmentDate.isPresent()){
            throw new AppointmentAlreadyExistsException(Msg.ALREADY_CREATEED);
        }

        Customer customer = customerRepo.findById(customerId).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
        Animal animal = animalRepo.findById(animalId).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));

        //Doktor available durumu kontrol edilir
        boolean isDoctorAvailable= doctor.getAvailableDates().stream()
                .anyMatch(availableDate -> availableDate.getAvailableDate().equals(appointment.getAppointmentDate()));

        if (!isDoctorAvailable){
            throw  new DoctorDoesNotAvailableException(Msg.NOT_AVAILABLE);

        }
        appointment.setCustomer(customer);
        appointment.setAnimal(animal);
        appointment.setDoctor(doctor);
        return appointmetRepo.save(appointment);
    }

    @Override
    public Appointment get(int id) {
        return appointmetRepo.findById(id).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Appointment> cursor(int page, int pageSie) {
        Pageable pageable = PageRequest.of(page,pageSie);
        return this.appointmetRepo.findAll(pageable);
    }

    @Override
    public Appointment update(Appointment appointment) {
        this.get(appointment.getId());
        return this.appointmetRepo.save(appointment);
    }

    @Override
    public boolean delete(int id) {
        Appointment appointment = this.get(id);
        this.appointmetRepo.delete(appointment);
        return true;
    }

    @Override
    public Optional<Appointment> get(LocalDateTime appointment) {
        return appointmetRepo.findByAppointmentDate(appointment);
    }
    @Override
    public List<Appointment> getAppointmentsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmetRepo.findByAppointmentDateBetween(startDate, endDate);
    }
}
