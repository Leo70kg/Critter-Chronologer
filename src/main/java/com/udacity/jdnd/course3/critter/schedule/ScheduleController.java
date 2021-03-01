package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleToScheduleDTO(scheduleService.addSchedule(convertScheduleDTOToSchedule(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList = scheduleService.getAllSchedules();
        return convertScheduleToScheduleDTO(scheduleList);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        List<Schedule> scheduleList = scheduleService.getSchedulesByPet(pet);

        return convertScheduleToScheduleDTO(scheduleList);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return convertScheduleToScheduleDTO(scheduleService.getScheduleByEmployeeId(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return convertScheduleToScheduleDTO(scheduleService.getScheduleByCustomerId(customerId));
    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        List<Long> employeeIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setEmployeeIds(employeeIds);
        return scheduleDTO;
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        List<Employee> employeeList = scheduleDTO.getEmployeeIds().stream().map(employeeService::getEmployeeById).collect(Collectors.toList());
        List<Pet> petList = scheduleDTO.getPetIds().stream().map(petService::getPetById).collect(Collectors.toList());
        schedule.setEmployees(employeeList);
        schedule.setPets(petList);

        return schedule;
    }

    private List<ScheduleDTO> convertScheduleToScheduleDTO(List<Schedule> scheduleList) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for (Schedule schedule: scheduleList) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }

        return scheduleDTOList;
    }
}
