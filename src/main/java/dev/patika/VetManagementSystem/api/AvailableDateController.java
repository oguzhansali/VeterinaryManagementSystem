package dev.patika.VetManagementSystem.api;

import dev.patika.VetManagementSystem.business.abtracts.IAvailableDateService;
import dev.patika.VetManagementSystem.business.abtracts.IDoctorService;
import dev.patika.VetManagementSystem.core.config.modelMapper.IModelMapperService;
import dev.patika.VetManagementSystem.core.result.Result;
import dev.patika.VetManagementSystem.core.result.ResultData;
import dev.patika.VetManagementSystem.core.utilies.ResultHelper;
import dev.patika.VetManagementSystem.dto.request.availableDate.AvailableDateSaveRequest;
import dev.patika.VetManagementSystem.dto.request.availableDate.AvailableDateUpdateRequest;
import dev.patika.VetManagementSystem.dto.response.CursorResponse;
import dev.patika.VetManagementSystem.dto.response.availableDate.AvailableDateResponse;
import dev.patika.VetManagementSystem.dto.response.doctor.DoctorResponse;
import dev.patika.VetManagementSystem.entity.AvailableDate;
import dev.patika.VetManagementSystem.entity.Doctor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/availableDate")
public class AvailableDateController {
    private  final IDoctorService doctorService;

    private final IModelMapperService modelMapper;
    private final IAvailableDateService availableDateService;

    public AvailableDateController(IDoctorService doctorService, IModelMapperService modelMapper, IAvailableDateService availableDateService) {
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
        this.availableDateService = availableDateService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableDateSaveRequest availableDateSaveRequest){
        AvailableDate saveAvailableDate = this.modelMapper.forRequest().map(availableDateSaveRequest,AvailableDate.class);

        Doctor doctor= this.doctorService.get(availableDateSaveRequest.getDoctorId());
        saveAvailableDate.setDoctor(doctor);


        this.availableDateService.save(saveAvailableDate);
        return ResultHelper.created(this.modelMapper.forResponse().map(saveAvailableDate,AvailableDateResponse.class));
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> get(@PathVariable("id")int id){
        AvailableDate availableDate = this.availableDateService.get(id);
        AvailableDateResponse availableDateResponse = this.modelMapper.forResponse().map(availableDate,AvailableDateResponse.class);
        return ResultHelper.success(availableDateResponse);
    }

    @GetMapping("/{id}/doctors")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> getDoctor(@PathVariable("id")int id){
        AvailableDate availableDate = this.availableDateService.get(id);
        return ResultHelper.success(this.modelMapper.forResponse().map(availableDate.getDoctor(),DoctorResponse.class));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AvailableDateResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<AvailableDate> availableDatePage = this.availableDateService.cursor(page, pageSize);
        Page<AvailableDateResponse> availableDateResponsePage = availableDatePage
                .map(availableDate -> this.modelMapper.forResponse().map(availableDate, AvailableDateResponse.class));

        return ResultHelper.cursor(availableDateResponsePage);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> update(@Valid @RequestBody AvailableDateUpdateRequest availableDateUpdateRequest){
        this.availableDateService.get(availableDateUpdateRequest.getId());
        AvailableDate updateAvailableDate= this.modelMapper.forRequest().map(availableDateUpdateRequest,AvailableDate.class);
        this.availableDateService.update(updateAvailableDate);
        return ResultHelper.success(this.modelMapper.forResponse().map(updateAvailableDate,AvailableDateResponse.class));
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id")int id){
        this.availableDateService.delete(id);
        return ResultHelper.ok();
    }



}
