package dev.patika.VetManagementSystem.business.abtracts;

import dev.patika.VetManagementSystem.entity.Animal;
import org.springframework.data.domain.Page;

public interface IAnimalService {
    Animal save(Animal animal);
    Animal get(int id);
    Page<Animal> cursor(int page, int pageSie);
    Animal update(Animal animal);
    boolean delete(int id);
}
