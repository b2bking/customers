package com.bmatyganov.service;

import java.util.Optional;

import com.bmatyganov.model.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Page<Customer> findAll(Pageable pageable);

    Optional<Customer> findOne(long id);

    void save(Customer customer);
}
