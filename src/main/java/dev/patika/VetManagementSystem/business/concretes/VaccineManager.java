package dev.patika.VetManagementSystem.business.concretes;

import dev.patika.VetManagementSystem.business.abtracts.IVaccineService;
import dev.patika.VetManagementSystem.core.config.modelMapper.IModelMapperService;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.AnimalRepo;
import dev.patika.VetManagementSystem.dao.VaccineRepo;
import dev.patika.VetManagementSystem.dto.response.vaccine.VaccineResponse;
import dev.patika.VetManagementSystem.entity.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VaccineManager implements IVaccineService {
    private final VaccineRepo vaccineRepo;
    private final AnimalRepo animalRepo;
    private final IModelMapperService modelMapper;

    // Yapıcı metod, VaccineRepo ve AnimalRepo bağımlılıklarını alır
    public VaccineManager(VaccineRepo vaccineRepo,AnimalRepo animalRepo,IModelMapperService modelMapper) {
        this.vaccineRepo = vaccineRepo;
        this.animalRepo=animalRepo;
        this.modelMapper=modelMapper;
    }

    @Override
    public Vaccine save(Vaccine vaccine) {
        // Vaccine nesnesinin ilişkilendirildiği Animal nesnesinin varlığını kontrol eder
        if (vaccine.getAnimal() == null || !animalRepo.existsById(vaccine.getAnimal().getAid())) {
            throw new NotFoundException(Msg.NOT_FOUND);
        }
        // Vaccine nesnesini kaydeder
        return vaccineRepo.save(vaccine);
    }

    @Override
    public Vaccine get(int id) {
        // Belirtilen ID'ye sahip Vaccine nesnesini getirir;
        // bulunamazsa NotFoundException fırlatır
        return vaccineRepo.findById(id).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Vaccine> cursor(int page, int pageSie) {
        // Sayfalama için Pageable nesnesi oluşturur ve bir sayfa vaccine verisini döner
        Pageable pageable = PageRequest.of(page,pageSie);
        return this.vaccineRepo.findAll(pageable);
    }

    @Override
    public Vaccine update(Vaccine vaccine) {
        // Güncellenmek istenen vaccine nesnesinin mevcut olup olmadığını kontrol eder
        this.get(vaccine.getId());
        // Vaccine nesnesinin bilgilerini günceller
        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public boolean delete(int id) {
        // Belirtilen ID'ye sahip Vaccine nesnesini getirir ve siler
        Vaccine vaccine = this.get(id);
        this.vaccineRepo.delete(vaccine);
        return true;
    }

    @Override
    public List<Vaccine> findByProtectionFnshDateBetween(LocalDate startDate, LocalDate endDate) {
        // Koruma bitiş tarihi belirtilen aralıkta olan vaccine'leri getirir
        return vaccineRepo.findByProtectionFnshDateBetween(startDate,endDate);
    }

   /* @Override
    public List<VaccineResponse> findVaccinesByProtectionFnshDateAndDateRange(LocalDate protectionFnshDate, LocalDate startDate, LocalDate endDate) {
        List<Vaccine> vaccines = vaccineRepo.findVaccinesByProtectionFnshDateAndDateRange(protectionFnshDate, startDate, endDate);

        return vaccines.stream().map(vaccine -> modelMapper.forResponse().map(vaccine,VaccineResponse.class)).collect(Collectors.toList());
    }*/
}
