package dev.patika.VetManagementSystem.api;

import dev.patika.VetManagementSystem.business.abtracts.IAvailableDateService;
import dev.patika.VetManagementSystem.business.abtracts.IDoctorService;
import dev.patika.VetManagementSystem.core.config.modelMapper.IModelMapperService;
import dev.patika.VetManagementSystem.core.result.Result;
import dev.patika.VetManagementSystem.core.result.ResultData;
import dev.patika.VetManagementSystem.core.utilies.ResultHelper;
import dev.patika.VetManagementSystem.dto.request.doctor.DoctorSaveRequests;
import dev.patika.VetManagementSystem.dto.request.doctor.DoctorUpdateRequest;
import dev.patika.VetManagementSystem.dto.response.CursorResponse;
import dev.patika.VetManagementSystem.dto.response.doctor.DoctorResponse;
import dev.patika.VetManagementSystem.entity.Doctor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/doctors")
public class DoctorController {
    private  final IDoctorService doctorService;
    private final IAvailableDateService availableDateService;

    private final IModelMapperService modelMapper;

    public DoctorController(IDoctorService doctorService, IModelMapperService modelMapper,IAvailableDateService availableDateService) {
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
        this.availableDateService= availableDateService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<DoctorResponse> save(@Valid @RequestBody DoctorSaveRequests doctorSaveRequests){
        Doctor saveDoctor = this.modelMapper.forRequest().map(doctorSaveRequests,Doctor.class);
        this.doctorService.save(saveDoctor);
        return ResultHelper.created(this.modelMapper.forResponse().map(saveDoctor,DoctorResponse.class));
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> get(@PathVariable("id") int id){
        Doctor doctor = this.doctorService.get(id);
        DoctorResponse doctorResponse = this.modelMapper.forResponse().map(doctor,DoctorResponse.class);
        return ResultHelper.success(doctorResponse);
    }
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<DoctorResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<Doctor> doctorPage = this.doctorService.cursor(page, pageSize);
        Page<DoctorResponse> doctorResponsePage = doctorPage
                .map(category -> this.modelMapper.forResponse().map(category, DoctorResponse.class));

        return ResultHelper.cursor(doctorResponsePage);
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> update(@Valid @RequestBody DoctorUpdateRequest doctorUpdateRequest){
        this.doctorService.get(doctorUpdateRequest.getId());
        Doctor updateDoctor=this.modelMapper.forRequest().map(doctorUpdateRequest,Doctor.class);
        this.doctorService.update(updateDoctor);
        return ResultHelper.success(this.modelMapper.forResponse().map(updateDoctor,DoctorResponse.class));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id")int id){
        this.doctorService.delete(id);
        return ResultHelper.ok();
    }




}
