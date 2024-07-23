package dev.patika.VetManagementSystem.business.concretes;


import dev.patika.VetManagementSystem.business.abtracts.IAvailableDateService;
import dev.patika.VetManagementSystem.core.exception.NotFoundException;
import dev.patika.VetManagementSystem.core.utilies.Msg;
import dev.patika.VetManagementSystem.dao.AvailableDateRepo;
import dev.patika.VetManagementSystem.entity.AvailableDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AvailableServiceManager implements IAvailableDateService {
        private final AvailableDateRepo availableDateRepo;

    public AvailableServiceManager(AvailableDateRepo availableDateRepo) {
        this.availableDateRepo = availableDateRepo;
    }

    @Override
    public AvailableDate save(AvailableDate availableDate) {
        return availableDateRepo.save(availableDate);
    }

    @Override
    public AvailableDate get(int id) {
        return availableDateRepo.findById(id).orElseThrow(()-> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<AvailableDate> cursor(int page, int pageSie) {
        Pageable pageable = PageRequest.of(page,pageSie);
        return this.availableDateRepo.findAll(pageable);
    }

    @Override
    public AvailableDate update(AvailableDate availableDate) {
        this.get(availableDate.getId());
        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public boolean delete(int id) {
        AvailableDate availableDate = this.get(id);
        this.availableDateRepo.delete(availableDate);
        return true;
    }
}
