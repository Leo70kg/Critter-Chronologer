package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.OwnerNotFoundException;
import com.udacity.jdnd.course3.critter.exception.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Pet addPet(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        Customer customer = savedPet.getCustomer();
        List<Pet> customerPets = customer.getPets();

        if (customerPets == null) {
            customerPets = new ArrayList<>();
        }
        customerPets.add(savedPet);
        customer.setPets(customerPets);
        customerRepository.save(customer);

        return savedPet;
    }

    public Pet getPetById(Long id) {
        Optional<Pet> optionalPet = petRepository.findById(id);
        if (optionalPet.isPresent()) {
            return optionalPet.get();
        }
        else {
            throw new PetNotFoundException();
        }
    }

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(ownerId);
        Customer customer = new Customer();

        if (optionalCustomer.isPresent()){
            customer = optionalCustomer.get();
        } else{
            throw new OwnerNotFoundException();
        }
        return petRepository.findPetsByCustomerEquals(customer);
    }
}
