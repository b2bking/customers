package com.bmatyganov.service;

import java.util.Optional;

import com.bmatyganov.model.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    public Page<Customer> findAll(Pageable pageable);

    public Optional<Customer> findOne(long id);

    public void save(Customer customer);
}
