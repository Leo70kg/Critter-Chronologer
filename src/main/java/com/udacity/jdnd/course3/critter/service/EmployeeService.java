package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.exception.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if(optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }else{
            throw new EmployeeNotFoundException();
        }
    }

    public void setAvailabilityById(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = getEmployeeById(employeeId);

        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    public List<Employee> getEmployeeService(EmployeeRequestDTO employeeRequestDTO) {
        return employeeRepository.findALLByDaysAvailable(employeeRequestDTO.getDate().getDayOfWeek())
                .stream()
                .filter(employee -> employee.getSkills().containsAll(employeeRequestDTO.getSkills()))
                .collect(Collectors.toList());
    }

}
