package dev.patika.VetManagementSystem.api;

import dev.patika.VetManagementSystem.business.abtracts.IAnimalService;
import dev.patika.VetManagementSystem.business.abtracts.IVaccineService;
import dev.patika.VetManagementSystem.core.config.modelMapper.IModelMapperService;
import dev.patika.VetManagementSystem.core.result.Result;
import dev.patika.VetManagementSystem.core.result.ResultData;
import dev.patika.VetManagementSystem.core.utilies.ResultHelper;
import dev.patika.VetManagementSystem.dto.request.vaccine.VaccineSaveRequest;
import dev.patika.VetManagementSystem.dto.request.vaccine.VaccineUpdateRequest;
import dev.patika.VetManagementSystem.dto.response.CursorResponse;
import dev.patika.VetManagementSystem.dto.response.appointment.AppointmentResponse;
import dev.patika.VetManagementSystem.dto.response.vaccine.VaccineResponse;
import dev.patika.VetManagementSystem.entity.Animal;
import dev.patika.VetManagementSystem.entity.Vaccine;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/v1/vaccines")
public class VaccineController {
    private final IVaccineService vaccineService;
    private final IModelMapperService modelMapper;
    private final IAnimalService animalService;

    public VaccineController(IVaccineService vaccineService, IModelMapperService modelMapper,IAnimalService animalService) {
        this.vaccineService = vaccineService;
        this.modelMapper = modelMapper;
        this.animalService=animalService;
    }

    // Yeni bir aşı kaydeder
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save(@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest){
        // Aşı verilerini DTO'dan Entity'ye dönüştürür
        Vaccine saveVaccine = this.modelMapper.forRequest().map(vaccineSaveRequest,Vaccine.class);
        saveVaccine.setId(0);
        Animal animal = this.animalService.get(vaccineSaveRequest.getAnimalId());
        saveVaccine.setAnimal(animal);
        // Aşıyı kaydeder
        this.vaccineService.save(saveVaccine);

        VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(saveVaccine,VaccineResponse.class);
        vaccineResponse.setAnimalId(animal.getAid());

        return ResultHelper.created(vaccineResponse);
    }
    // Belirli bir aşının bilgilerini alır
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> get (@PathVariable("id")int id){
        Vaccine vaccine = this.vaccineService.get(id);
        VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(vaccine,VaccineResponse.class);
        return ResultHelper.success(vaccineResponse);

    }

    // Aşıların sayfalı listesini alır
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<VaccineResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<Vaccine> vaccinePage = this.vaccineService.cursor(page, pageSize);
        Page<VaccineResponse> vaccineResponsePage = vaccinePage
                .map(vaccine -> this.modelMapper.forResponse().map(vaccine, VaccineResponse.class));

        return ResultHelper.cursor(vaccineResponsePage);
    }

    // Belirli bir aşının bilgisini günceller
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> update(@Valid @RequestBody VaccineUpdateRequest vaccineUpdateRequest){
        this.vaccineService.get(vaccineUpdateRequest.getId());
        Vaccine updateVaccine = this.modelMapper.forRequest().map(vaccineUpdateRequest,Vaccine.class);

        Animal animal = this.animalService.get(vaccineUpdateRequest.getAnimalId());
        updateVaccine.setAnimal(animal);

        this.vaccineService.update(updateVaccine);

        VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(updateVaccine,VaccineResponse.class);
        vaccineResponse.setAnimalId(animal.getAid());
        return ResultHelper.success(vaccineResponse);
    }

    // Belirli bir aşının bilgisini siler
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id")int id){
        this.vaccineService.delete(id);
        return ResultHelper.ok();
    }

    //Girilen tarih aralığında  protectionFnshDate'i olan vaccineleri getirir.
    @GetMapping("/byProtectionFnshDate/{protectionFnshDate}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> findVaccinesByProtectionFnshDateAndDateRange(
            @PathVariable("protectionFnshDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate protectionFnshDate,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<VaccineResponse> vaccineResponses = vaccineService.findVaccinesByProtectionFnshDateAndDateRange(protectionFnshDate, startDate, endDate);
        return ResultHelper.success(vaccineResponses);
    }








}
