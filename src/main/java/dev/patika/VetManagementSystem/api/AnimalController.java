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
import dev.patika.VetManagementSystem.dto.response.availableDate.AvailableDateResponse;
import dev.patika.VetManagementSystem.dto.response.customer.CustomerResponse;
import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.AvailableDate;
import dev.patika.VetManagementSystem.entity.Customer;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/animals")
public class AnimalController {
    private final ICustomerService customerService;
    private final IAnimalService animalService;
    private final IModelMapperService modelMapper;

    public AnimalController(ICustomerService customerService,
                            IAnimalService animalService,
                            IModelMapperService modelMapper) {
        this.customerService = customerService;
        this.animalService = animalService;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save (@Valid @RequestBody AnimalSaveRequest animalSaveRequest){
        Animal saveAnimal = this.modelMapper.forRequest().map(animalSaveRequest,Animal.class);

        Customer customer = this.customerService.get(animalSaveRequest.getCustomerId());
        saveAnimal.setCustomer(customer);

        this.animalService.save(saveAnimal);
        return ResultHelper.created(this.modelMapper.forResponse().map(saveAnimal,AnimalResponse.class));
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> get(@PathVariable("id")int id){
        Customer customer = this.customerService.get(id);
        CustomerResponse customerResponse = this.modelMapper.forResponse().map(customer,CustomerResponse.class);
        return ResultHelper.success(customerResponse);
    }
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

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> update(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest){
        this.animalService.get(animalUpdateRequest.getId());
        Animal updateAnimal = this.modelMapper.forRequest().map(animalUpdateRequest,Animal.class);
        this.animalService.update(updateAnimal);
        return ResultHelper.success(this.modelMapper.forResponse().map(updateAnimal,AnimalResponse.class));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id")int id){
        this.animalService.delete(id);
        return ResultHelper.ok();
    }


}
