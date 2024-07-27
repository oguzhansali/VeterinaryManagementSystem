package dev.patika.VetManagementSystem.core.config.modelMapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    // ModelMapper nesnesinin Spring'in IoC konteynerine bir bean olarak eklenmesini sağlar
    @Bean
    public ModelMapper getModelMapper(){
        // Yeni bir ModelMapper örneği oluşturur ve döner
        return new ModelMapper();
    }
}
