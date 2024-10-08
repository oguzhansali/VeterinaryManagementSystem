package dev.patika.VetManagementSystem.api;

import dev.patika.VetManagementSystem.business.abtracts.IAnimalService;
import dev.patika.VetManagementSystem.business.abtracts.ICustomerService;
import dev.patika.VetManagementSystem.core.config.modelMapper.IModelMapperService;
import dev.patika.VetManagementSystem.core.result.Result;
import dev.patika.VetManagementSystem.core.result.ResultData;
import dev.patika.VetManagementSystem.core.utilies.ResultHelper;
import dev.patika.VetManagementSystem.dto.request.animal.AnimalSaveRequest;
import dev.patika.VetManagementSystem.dto.request.animal.AnimalUpdateRequest;
import dev.patika.VetManagementSystem.dto.response.CursorResponse;
import dev.patika.VetManagementSystem.dto.response.animal.AnimalResponse;
import dev.patika.VetManagementSystem.dto.response.customer.CustomerResponse;
import dev.patika.VetManagementSystem.dto.response.vaccine.VaccineResponse;
import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.Customer;
import dev.patika.VetManagementSystem.entity.Vaccine;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/animals")
public class AnimalController {
    private final ICustomerService customerService;
    private final IAnimalService animalService;
    private final IModelMapperService modelMapper;
    private final ModelMapper modelMap;


    public AnimalController(ICustomerService customerService,
                            IAnimalService animalService,
                            IModelMapperService modelMapper,
                            ModelMapper modelMap) {
        this.customerService = customerService;
        this.animalService = animalService;
        this.modelMapper = modelMapper;
        this.modelMap=modelMap;
    }

    //Yeni bir hayvan kaydetme isteği alır
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        Animal saveAnimal = this.modelMapper.forRequest().map(animalSaveRequest,Animal.class);
        // İlgili müşteri bilgilerini alır
        Customer customer = this.customerService.get(animalSaveRequest.getCustomerId());
        saveAnimal.setCustomer(customer);
        // Hayvanı kaydeder
        this.animalService.save(saveAnimal);
        return ResultHelper.created(this.modelMapper.forResponse().map(saveAnimal, AnimalResponse.class));
    }

    // Belirli bir hayvanın bilgilerini alır
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> get(@PathVariable("id") int id) {
        Animal animal = this.animalService.get(id);
        AnimalResponse animalResponse = this.modelMapper.forResponse().map(animal, AnimalResponse.class);
        return ResultHelper.success(animalResponse);
    }

    // Hayvanların sayfalı listesini alır
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AnimalResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<Animal> animalPage = this.animalService.cursor(page, pageSize);
        Page<AnimalResponse> animalResponsePage = animalPage
                .map(animal -> this.modelMapper.forResponse().map(animal, AnimalResponse.class));

        return ResultHelper.cursor(animalResponsePage);
    }

    // Belirli bir hayvanın bilgilerini günceller
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> update(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        this.animalService.get(animalUpdateRequest.getAid());
        Animal updateAnimal = this.modelMapper.forRequest().map(animalUpdateRequest, Animal.class);
        Customer customer =this.customerService.get(animalUpdateRequest.getCustomerId());
        updateAnimal.setCustomer(customer);
        this.animalService.update(updateAnimal);
        AnimalResponse animalResponse  =this.modelMapper.forResponse().map(updateAnimal,AnimalResponse.class);
        animalResponse.setCustomerId(customer.getId());
        return ResultHelper.success(animalResponse);
    }

    // Belirli bir hayvanı siler
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id) {
        this.animalService.delete(id);
        return ResultHelper.ok();
    }

    // Belirli bir hayvanın aşı bilgilerini alır
    @GetMapping("/{id}/vaccine")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccine(@PathVariable("id") int id) {
        Animal animal = this.animalService.get(id);
        List<Vaccine> vaccines = animal.getVaccines();

        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> this.modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(vaccineResponses);
    }

    //Hayvan ismine göre filtreleme yapar
    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> getByName(@PathVariable("name") String name) {
        Animal animal = this.animalService.getByName(name);
        AnimalResponse animalResponse = this.modelMapper.forResponse().map(animal, AnimalResponse.class);
        return ResultHelper.success(animalResponse);
    }


}
