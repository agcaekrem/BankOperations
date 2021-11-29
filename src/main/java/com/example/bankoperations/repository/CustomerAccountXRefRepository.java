package com.example.bankoperations.repository;

import com.example.bankoperations.model.CustomerAccountXRef;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAccountXRefRepository extends CrudRepository<CustomerAccountXRef, String> {

}
