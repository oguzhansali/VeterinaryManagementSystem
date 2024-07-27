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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        this.appointmentService=appointmentService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save (@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest){
        Appointment saveAppointment = this.modelMapper.forRequest().map(appointmentSaveRequest,Appointment.class);

        Doctor doctor = this.doctorService.get(appointmentSaveRequest.getDoctorId());
        saveAppointment.setDoctor(doctor);


        Animal animal = this.animalService.get(appointmentSaveRequest.getAnimalId());
        saveAppointment.setAnimal(animal);

        Appointment savedAppointment = this.appointmentService.save(saveAppointment,
                appointmentSaveRequest.getAnimalId(),
                appointmentSaveRequest.getDoctorId());

        return ResultHelper.created(this.modelMapper.forResponse().map(saveAppointment,AppointmentResponse.class));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> get(@PathVariable("id")int id){
        Appointment appointment = this.appointmentService.get(id);
        AppointmentResponse appointmentResponse = this.modelMapper.forResponse().map(appointment,AppointmentResponse.class);
        return ResultHelper.success(appointmentResponse);
    }

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

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(@Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest){
        this.appointmentService.get(appointmentUpdateRequest.getId());
        Appointment updateAppointment=this.modelMapper.forRequest().map(appointmentUpdateRequest,Appointment.class);
        this.appointmentService.update(updateAppointment);
        return ResultHelper.success(this.modelMapper.forResponse().map(updateAppointment,AppointmentResponse.class));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id")int id){
        this.appointmentService.delete(id);
        return ResultHelper.ok();
    }

    @GetMapping("/{appointmentDate}/animal")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> getAnimal(@PathVariable("appointmentDate")LocalDateTime appointmentDate){
        Optional<Appointment> appointmentOptional = appointmentService.get(appointmentDate);
        Appointment appointment=appointmentOptional.get();
        return ResultHelper.success(this.modelMapper.forResponse().map(appointment.getAnimal(),AnimalResponse.class));
    }
    @GetMapping("/{appointmentDate}/doctor")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> getDoctor(@PathVariable("appointmentDate")LocalDateTime appointmentDate){
        Optional<Appointment> appointmentOptional = appointmentService.get(appointmentDate);
        Appointment appointment=appointmentOptional.get();
        return ResultHelper.success(this.modelMapper.forResponse().map(appointment.getDoctor(),DoctorResponse.class));
    }

 /*   @GetMapping("/animals")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<Animal>> getAnimal(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Appointment> appointments = appointmentService.getAppointmentsWithinDateRange(startDate, endDate);

        List<Animal> animals = appointments.stream()
                .map(Appointment::getAnimal)
                .distinct()  // Doktorları benzersiz hale getirmek için
                .collect(Collectors.toList());

        return ResultHelper.success(animals);
    }*/
    @GetMapping("/animals")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimals(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        List<AnimalResponse> animalResponses = animalService.getAnimalsWithinDateRange(startDate,endDate);
        return ResultHelper.success(animalResponses);
    }



    @GetMapping("/doctors")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<DoctorResponse>> getDoctors(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DoctorResponse> doctorResponses = doctorService.getDoctorsWithinDateRange(startDate, endDate);
        return ResultHelper.success(doctorResponses);
    }



}
