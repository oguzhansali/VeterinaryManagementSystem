package dev.patika.VetManagementSystem.core.config.modelMapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelManagerService implements IModelMapperService{
    private final ModelMapper modelMapper;

    @Autowired
    public  ModelManagerService(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }

    @Override
    public ModelMapper forRequest() {
        // ModelMapper konfigürasyonunu, belirsizlikleri göz ardı edecek ve
        // eşleşme stratejisini STANDARD olarak ayarlayacak şekilde yapılandırır
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.STANDARD);
        return this.modelMapper;
    }

    @Override
    public ModelMapper forResponse() {
        // ModelMapper konfigürasyonunu, belirsizlikleri göz ardı edecek ve
        // eşleşme stratejisini LOOSE olarak ayarlayacak şekilde yapılandırır
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.LOOSE);
        return this.modelMapper;
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

}
