package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exception.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CustomerRepository customerRepository;

    public Schedule addSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesByPet(Pet pet) {
        return scheduleRepository.getSchedulesByPets(pet);
    }

    public List<Schedule> getScheduleByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        return scheduleRepository.getSchedulesByEmployees(employee);
    }

    public List<Schedule> getScheduleByCustomerId(Long customerId) {
        List<Schedule> scheduleList = new ArrayList<>();
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new CustomerNotFoundException();
        }
        List<Pet> petList = petRepository.findPetsByCustomerEquals(customer.get());
        petList.forEach(pet -> scheduleList.addAll(scheduleRepository.getSchedulesByPets(pet)));

        return scheduleList;
    }

}
