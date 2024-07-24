package dev.patika.VetManagementSystem.business.concretes;

import dev.patika.VetManagementSystem.business.abtracts.IVaccineService;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.VaccineRepo;
import dev.patika.VetManagementSystem.entity.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VaccineManager implements IVaccineService {
    private final VaccineRepo vaccineRepo;

    public VaccineManager(VaccineRepo vaccineRepo) {
        this.vaccineRepo = vaccineRepo;
    }

    @Override
    public Vaccine save(Vaccine vaccine) {
        return vaccineRepo.save(vaccine);
    }

    @Override
    public Vaccine get(int id) {
        return vaccineRepo.findById(id).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Vaccine> cursor(int page, int pageSie) {
        Pageable pageable = PageRequest.of(page,pageSie);
        return this.vaccineRepo.findAll(pageable);
    }

    @Override
    public Vaccine update(Vaccine vaccine) {
        this.get(vaccine.getId());
        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public boolean delete(int id) {
        Vaccine vaccine = this.get(id);
        this.vaccineRepo.delete(vaccine);
        return true;
    }
}
