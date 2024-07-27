package dev.patika.VetManagementSystem.core.config.modelMapper;

import dev.patika.VetManagementSystem.dto.response.animal.AnimalResponse;
import dev.patika.VetManagementSystem.entity.Animal;
import org.modelmapper.ModelMapper;

public interface IModelMapperService {
    ModelMapper forRequest();
    ModelMapper forResponse();

}
