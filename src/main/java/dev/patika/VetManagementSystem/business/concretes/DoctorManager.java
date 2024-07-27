package dev.patika.VetManagementSystem.business.concretes;

import dev.patika.VetManagementSystem.business.abtracts.IAppointmentService;
import dev.patika.VetManagementSystem.business.abtracts.IDoctorService;
import dev.patika.VetManagementSystem.core.config.modelMapper.IModelMapperService;
import dev.patika.VetManagementSystem.core.exception.EmailAlreadyExistsException;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.AppointmetRepo;
import dev.patika.VetManagementSystem.dao.AvailableDateRepo;
import dev.patika.VetManagementSystem.dao.DoctorRepo;
import dev.patika.VetManagementSystem.dto.response.doctor.DoctorResponse;
import dev.patika.VetManagementSystem.entity.Appointment;
import dev.patika.VetManagementSystem.entity.Doctor;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorManager implements IDoctorService {
    private final DoctorRepo doctorRepo;
    private final AvailableDateRepo availableDateRepo;
    private final AppointmetRepo appointmetRepo;
    private final IAppointmentService appointmentService;
    private final IModelMapperService modelMapper;

    // Doktor, AvailableDate, Randevu ve ModelMapper servislerini enjekte eden yapıcı metod
    public DoctorManager(DoctorRepo doctorRepo, AvailableDateRepo availableDateRepo, AppointmetRepo appointmetRepo, IAppointmentService appointmentService, IModelMapperService modelMapper) {
        this.doctorRepo = doctorRepo;
        this.availableDateRepo = availableDateRepo;
        this.appointmetRepo = appointmetRepo;
        this.appointmentService = appointmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Doctor save(Doctor doctor) {
        // E-posta adresi ile mevcut doktor olup olmadığını kontrol eder
        Optional<Doctor> existingDoctorByMail = doctorRepo.findByMail(doctor.getMail());
        if (existingDoctorByMail.isPresent()) {
            throw new EmailAlreadyExistsException(Msg.ALREADY_CREATEED);
        }
        // Yeni doktoru kaydeder
        return doctorRepo.save(doctor);
    }

    @Override
    public Doctor get(int id) {
        // Verilen ID'ye sahip doktor nesnesini getirir; bulunamazsa NotFoundException fırlatır
        return doctorRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Doctor update(Doctor doctor) {
        // Güncellenmek istenen doktorun mevcut olup olmadığını kontrol eder
        this.get(doctor.getId());
        // Doktorun bilgilerini günceller
        return this.doctorRepo.save(doctor);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        // Verilen ID'ye sahip doktoru getirir; bulunamazsa NotFoundException fırlatır
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));

        // Doktorla ilişkili tüm randevuları ve uygunluk tarihlerini siler
        appointmetRepo.deleteByDoctorId(id);
        availableDateRepo.deleteByDoctorId(id);
        // Doktoru siler
        doctorRepo.delete(doctor);
        return true;
    }

    @Override
    public Page<Doctor> cursor(int page, int pageSie) {
        // Sayfalama için Pageable nesnesi oluşturur ve bir sayfa doktor verisini döner
        Pageable pageable = PageRequest.of(page, pageSie);
        return this.doctorRepo.findAll(pageable);
    }

    @Override
    public List<DoctorResponse> getDoctorsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // Belirtilen tarih aralığında randevuları alır
        List<Appointment> appointments = appointmentService.getAppointmentsWithinDateRange(startDate, endDate);

        // Randevulardan doktorları toplar ve benzersiz doktorları elde eder
        Set<Doctor> doctorSet = appointments.stream()
                .map(Appointment::getDoctor)
                .collect(Collectors.toSet());  // Set kullanarak benzersiz doktorları topla

        // Benzersiz doktorları listeye dönüştürür
        List<Doctor> doctors = new ArrayList<>(doctorSet);  // Set'i List'e dönüştür

        // Doktorları DTO'ya dönüştürür ve döner
        return doctors.stream()
                .map(doctor -> modelMapper.forResponse().map(doctor, DoctorResponse.class))
                .collect(Collectors.toList());

    }
}
