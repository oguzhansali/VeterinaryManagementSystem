package dev.patika.VetManagementSystem.business.concretes;

import dev.patika.VetManagementSystem.business.abtracts.IDoctorService;
import dev.patika.VetManagementSystem.core.exception.EmailAlreadyExistsException;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.AppointmetRepo;
import dev.patika.VetManagementSystem.dao.AvailableDateRepo;
import dev.patika.VetManagementSystem.dao.DoctorRepo;
import dev.patika.VetManagementSystem.entity.Doctor;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorManager implements IDoctorService {
    private final DoctorRepo doctorRepo;
    private final AvailableDateRepo availableDateRepo;
    private final AppointmetRepo appointmetRepo;

    public DoctorManager(DoctorRepo doctorRepo, AvailableDateRepo availableDateRepo,AppointmetRepo appointmetRepo) {
        this.doctorRepo = doctorRepo;
        this.availableDateRepo=availableDateRepo;
        this.appointmetRepo=appointmetRepo;
    }

    @Override
    public Doctor save(Doctor doctor) {
        Optional<Doctor> existingDoctorByMail = doctorRepo.findByMail(doctor.getMail());
        if (existingDoctorByMail.isPresent()) {
            throw new EmailAlreadyExistsException(Msg.ALREADY_CREATEED);
        }
        return doctorRepo.save(doctor);
    }

    @Override
    public Doctor get(int id) {
        return doctorRepo.findById(id).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Doctor update(Doctor doctor) {
        this.get(doctor.getId());
        return this.doctorRepo.save(doctor);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));

        appointmetRepo.deleteByDoctorId(id);
        availableDateRepo.deleteByDoctorId(id);
        doctorRepo.delete(doctor);
        return true;
    }

    @Override
    public Page<Doctor> cursor(int page, int pageSie) {
        Pageable pageable = PageRequest.of(page,pageSie);
        return this.doctorRepo.findAll(pageable);
    }

}
