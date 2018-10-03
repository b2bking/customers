package com.bmatyganov.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bmatyganov.model.ApplicationUser;
import com.bmatyganov.model.Customer;
import com.bmatyganov.repository.ApplicationUserRepository;
import com.bmatyganov.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @GetMapping(value = "/customers")
    List<Customer> findAll(@RequestParam(required=false) Long id){
        List<Customer> customers = new ArrayList<>();
        if(null == id){
            Iterable<Customer> results = this.customerRepository.findAll();
            results.forEach(customer -> customers.add(customer));
        }else{
            Optional<Customer> customer = this.customerRepository.findById(id);
            if(customer.isPresent()){
                customers.add(customer.get());
            }
        }

        return customers;
    }

    @GetMapping(value = "/users")
    List<ApplicationUser> findUser(@RequestParam(required=false) Long id){
        List<ApplicationUser> customers = new ArrayList<>();
        if(null == id){
            Iterable<ApplicationUser> results = this.applicationUserRepository.findAll();
            results.forEach(customer -> customers.add(customer));
        }else{
            Optional<ApplicationUser> customer = this.applicationUserRepository.findById(id);
            if(customer.isPresent()){
                customers.add(customer.get());
            }
        }

        return customers;
    }
}
