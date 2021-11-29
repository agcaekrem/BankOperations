package com.example.bankoperations.service;

import com.example.bankoperations.domain.AccountInformation;
import com.example.bankoperations.domain.CustomerDetails;

import com.example.bankoperations.domain.TransactionDetails;
import com.example.bankoperations.domain.TransferDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BankingService {

    public List<CustomerDetails> findAll();

    public ResponseEntity<Object> addCustomer(CustomerDetails customerDetails);

    public CustomerDetails findByCustomerNumber(Long customerNumber);

    public ResponseEntity<Object> updateCustomer(CustomerDetails customerDetails, Long customerNumber);

    public ResponseEntity<Object> deleteCustomer(Long customerNumber) ;

    public ResponseEntity<Object> findByAccountNumber(Long accountNumber);

    public ResponseEntity<Object> addNewAccount(AccountInformation accountInformation, Long customerNumber);

    public ResponseEntity<Object> transferDetails(TransferDetails transferDetails, Long customerNumber);

    public List<TransactionDetails> findTransactionsByAccountNumber(Long accountNumber);

}