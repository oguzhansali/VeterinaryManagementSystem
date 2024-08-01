package dev.patika.VetManagementSystem.api;

import dev.patika.VetManagementSystem.business.abtracts.IAnimalService;
import dev.patika.VetManagementSystem.business.abtracts.IAppointmentService;
import dev.patika.VetManagementSystem.business.abtracts.ICustomerService;
import dev.patika.VetManagementSystem.business.abtracts.IDoctorService;
import dev.patika.VetManagementSystem.core.config.modelMapper.IModelMapperService;
import dev.patika.VetManagementSystem.core.result.Result;
import dev.patika.VetManagementSystem.core.result.ResultData;
import dev.patika.VetManagementSystem.core.utilies.ResultHelper;
import dev.patika.VetManagementSystem.dto.request.appointment.AppointmentSaveRequest;
import dev.patika.VetManagementSystem.dto.request.appointment.AppointmentUpdateRequest;
import dev.patika.VetManagementSystem.dto.response.CursorResponse;
import dev.patika.VetManagementSystem.dto.response.animal.AnimalResponse;
import dev.patika.VetManagementSystem.dto.response.appointment.AppointmentResponse;
import dev.patika.VetManagementSystem.dto.response.doctor.DoctorResponse;
import dev.patika.VetManagementSystem.entity.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {
    private final IDoctorService doctorService;
    private final ICustomerService customerService;
    private final IAnimalService animalService;
    private final IModelMapperService modelMapper;
    private final IAppointmentService appointmentService;

    public AppointmentController(IDoctorService doctorService,
                                 ICustomerService customerService,
                                 IAnimalService animalService,
                                 IModelMapperService modelMapper,
                                 IAppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.customerService = customerService;
        this.animalService = animalService;
        this.modelMapper = modelMapper;
        this.appointmentService = appointmentService;
    }

    // Yeni bir randevu kaydetme isteği alır
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest) {
        // Randevu verilerini DTO'dan Entity'ye dönüştürür
        Appointment saveAppointment = this.modelMapper.forRequest().map(appointmentSaveRequest, Appointment.class);
        // İlgili doktor ve hayvan bilgilerini alır
        Doctor doctor = this.doctorService.get(appointmentSaveRequest.getDoctorId());
        saveAppointment.setDoctor(doctor);
        Animal animal = this.animalService.get(appointmentSaveRequest.getAnimalId());
        saveAppointment.setAnimal(animal);

        // Randevuyu kaydeder
        Appointment savedAppointment = this.appointmentService.save(saveAppointment,
                appointmentSaveRequest.getAnimalId(),
                appointmentSaveRequest.getDoctorId());

        return ResultHelper.created(this.modelMapper.forResponse().map(saveAppointment, AppointmentResponse.class));
    }

    // Belirli bir randevunun bilgilerini alır
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> get(@PathVariable("id") int id) {
        Appointment appointment = this.appointmentService.get(id);
        AppointmentResponse appointmentResponse = this.modelMapper.forResponse().map(appointment, AppointmentResponse.class);
        return ResultHelper.success(appointmentResponse);
    }

    // Randevuların sayfalı listesini alır
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AppointmentResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<Appointment> appointmentPage = this.appointmentService.cursor(page, pageSize);
        Page<AppointmentResponse> appointmentResponsePage = appointmentPage
                .map(appointment -> this.modelMapper.forResponse().map(appointment, AppointmentResponse.class));

        return ResultHelper.cursor(appointmentResponsePage);
    }

    // Belirli bir randevuyu günceller
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(@Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) {
        this.appointmentService.get(appointmentUpdateRequest.getId());
        Appointment updateAppointment = this.modelMapper.forRequest().map(appointmentUpdateRequest, Appointment.class);
        this.appointmentService.update(updateAppointment);
        return ResultHelper.success(this.modelMapper.forResponse().map(updateAppointment, AppointmentResponse.class));
    }

    // Belirli bir randevuyu siler
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id) {
        this.appointmentService.delete(id);
        return ResultHelper.ok();
    }

    // Belirli bir tarihteki randevunun hayvan bilgilerini alır
    @GetMapping("/{appointmentDate}/animal")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> getAnimal(@PathVariable("appointmentDate") LocalDateTime appointmentDate) {
        Optional<Appointment> appointmentOptional = appointmentService.get(appointmentDate);
        Appointment appointment = appointmentOptional.get();
        return ResultHelper.success(this.modelMapper.forResponse().map(appointment.getAnimal(), AnimalResponse.class));
    }

    // Belirli bir tarihteki randevunun doktor bilgilerini alır
    @GetMapping("/{appointmentDate}/doctor")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> getDoctor(@PathVariable("appointmentDate") LocalDateTime appointmentDate) {
        Optional<Appointment> appointmentOptional = appointmentService.get(appointmentDate);
        Appointment appointment = appointmentOptional.get();
        return ResultHelper.success(this.modelMapper.forResponse().map(appointment.getDoctor(), DoctorResponse.class));
    }

    // Belirli bir tarih aralığındaki randevularda yer alan hayvanları alır
    @GetMapping("/animals")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimals(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AnimalResponse> animalResponses = animalService.getAnimalsWithinDateRange(startDate, endDate);
        return ResultHelper.success(animalResponses);
    }

    // Belirli bir tarih aralığındaki randevularda yer alan doktorları alır
    @GetMapping("/doctors")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<DoctorResponse>> getDoctors(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DoctorResponse> doctorResponses = doctorService.getDoctorsWithinDateRange(startDate, endDate);
        return ResultHelper.success(doctorResponses);
    }


    @GetMapping("/doctors/{doctorId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> getAppointmentsByDoctorIdAndDateRange(
            @PathVariable("doctorId") int doctorId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ){
        List<AppointmentResponse> appointmentResponses = appointmentService.findAppointmentsByDoctorIdAndDateRange(doctorId, startDate, endDate);
        return ResultHelper.success(appointmentResponses);
    }


}
