package com.example.bankoperations.service;


import com.example.bankoperations.domain.AccountInformation;
import com.example.bankoperations.domain.CustomerDetails;
import com.example.bankoperations.domain.TransactionDetails;
import com.example.bankoperations.domain.TransferDetails;
import com.example.bankoperations.model.*;
import com.example.bankoperations.repository.AccountRepository;
import com.example.bankoperations.repository.CustomerAccountXRefRepository;
import com.example.bankoperations.repository.CustomerRepository;
import com.example.bankoperations.repository.TransactionRepository;
import com.example.bankoperations.service.helper.BankingServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BankingServiceImpl implements BankingService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private CustomerAccountXRefRepository custAccXRefRepository;
	@Autowired
	private BankingServiceHelper bankingServiceHelper;

	public BankingServiceImpl(CustomerRepository repository) {
		this.customerRepository=repository;
	}


	public List<CustomerDetails> findAll() {

		List<CustomerDetails> allCustomerDetails = new ArrayList<>();

		Iterable<Customer> customerList = customerRepository.findAll();

		customerList.forEach(customer -> {
			allCustomerDetails.add(bankingServiceHelper.convertToCustomerDomain(customer));
		});

		return allCustomerDetails;
	}

	/**
	 * CREATE Customer
	 *
	 */
	public ResponseEntity<Object> addCustomer(CustomerDetails customerDetails) {

		Customer customer = bankingServiceHelper.convertToCustomerEntity(customerDetails);
		customer.setCreateDateTime(new Date());
		customerRepository.save(customer);

		return ResponseEntity.status(HttpStatus.CREATED).body("New Customer created successfully.");
	}

	/**
	 * READ Customer
	 *
	 */

	public CustomerDetails findByCustomerNumber(Long customerNumber) {

		Optional<Customer> customerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);

		if(customerEntityOpt.isPresent())
			return bankingServiceHelper.convertToCustomerDomain(customerEntityOpt.get());

		return null;
	}

	/**
	 * UPDATE Customer
	 *
	 */
	public ResponseEntity<Object> updateCustomer(CustomerDetails customerDetails, Long customerNumber) {
		Optional<Customer> managedCustomerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);
		Customer unmanagedCustomerEntity = bankingServiceHelper.convertToCustomerEntity(customerDetails);
		if(managedCustomerEntityOpt.isPresent()) {
			Customer managedCustomerEntity = managedCustomerEntityOpt.get();

			if(Optional.ofNullable(unmanagedCustomerEntity.getContactDetails()).isPresent()) {

				Contact managedContact = managedCustomerEntity.getContactDetails();
				if(managedContact != null) {
					managedContact.setEmailId(unmanagedCustomerEntity.getContactDetails().getEmailId());
					managedContact.setHomePhone(unmanagedCustomerEntity.getContactDetails().getHomePhone());
					managedContact.setWorkPhone(unmanagedCustomerEntity.getContactDetails().getWorkPhone());
				} else
					managedCustomerEntity.setContactDetails(unmanagedCustomerEntity.getContactDetails());
			}



			managedCustomerEntity.setUpdateDateTime(new Date());
			managedCustomerEntity.setStatus(unmanagedCustomerEntity.getStatus());
			managedCustomerEntity.setFirstName(unmanagedCustomerEntity.getFirstName());
			managedCustomerEntity.setMiddleName(unmanagedCustomerEntity.getMiddleName());
			managedCustomerEntity.setLastName(unmanagedCustomerEntity.getLastName());
			managedCustomerEntity.setUpdateDateTime(new Date());

			customerRepository.save(managedCustomerEntity);

			return ResponseEntity.status(HttpStatus.OK).body("Success: Customer updated.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Number " + customerNumber + " not found.");
		}
	}

	/**
	 * DELETE Customer
	 */
	public ResponseEntity<Object> deleteCustomer(Long customerNumber) {

		Optional<Customer> managedCustomerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);

		if(managedCustomerEntityOpt.isPresent()) {
			Customer managedCustomerEntity = managedCustomerEntityOpt.get();
			customerRepository.delete(managedCustomerEntity);
			return ResponseEntity.status(HttpStatus.OK).body("Success: Customer deleted.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer does not exist.");
		}

	}

	/**
	 * Find Account
	 *
	 */
	public ResponseEntity<Object> findByAccountNumber(Long accountNumber) {

		Optional<Account> accountEntityOpt = accountRepository.findByAccountNumber(accountNumber);

		if(accountEntityOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.FOUND).body(bankingServiceHelper.convertToAccountDomain(accountEntityOpt.get()));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Number " + accountNumber + " not found.");
		}

	}

	/**
	 * Create new account
	 *
	 */
	public ResponseEntity<Object> addNewAccount(AccountInformation accountInformation, Long customerNumber) {

		Optional<Customer> customerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);

		if(customerEntityOpt.isPresent()) {
			accountRepository.save(bankingServiceHelper.convertToAccountEntity(accountInformation));

			// CustomerAccountXRef'e bir giriş ekleyin
			custAccXRefRepository.save(CustomerAccountXRef.builder()
					.accountNumber(accountInformation.getAccountNumber())
					.customerNumber(customerNumber)
					.build());

		}

		return ResponseEntity.status(HttpStatus.CREATED).body("New Account created successfully.");
	}

	/**
	 * Transfer funds from one account to another for a specific customer
	 *
	 */
	public ResponseEntity<Object> transferDetails(TransferDetails transferDetails, Long customerNumber) {

		List<Account> accountEntities = new ArrayList<>();
		Account fromAccountEntity = null;
		Account toAccountEntity = null;

		Optional<Customer> customerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);

		// Eğer müşteri varsa
		if(customerEntityOpt.isPresent()) {

			// Hesap bilgisini al
			Optional<Account> fromAccountEntityOpt = accountRepository.findByAccountNumber(transferDetails.getFromAccountNumber());
			if(fromAccountEntityOpt.isPresent()) {
				fromAccountEntity = fromAccountEntityOpt.get();
			}
			else {
				// Request(istekten) mevcut değilse 404 not found hatası
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("From Account Number " + transferDetails.getFromAccountNumber() + " not found.");
			}


			// Hesap bilgisine ulaş
			Optional<Account> toAccountEntityOpt = accountRepository.findByAccountNumber(transferDetails.getToAccountNumber());
			if(toAccountEntityOpt.isPresent()) {
				toAccountEntity = toAccountEntityOpt.get();
			}
			else {
				// Request(istek) mevcut değilse --> 404 hatalı istek
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("To Account Number " + transferDetails.getToAccountNumber() + " not found.");
			}


			// Yeterli para yoksa --> 400 hatalı istek..iade edin
			if(fromAccountEntity.getAccountBalance() < transferDetails.getTransferAmount()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Funds.");
			}
			else {
				synchronized (this) {
					// Hesaptan güncelleme
					fromAccountEntity.setAccountBalance(fromAccountEntity.getAccountBalance() - transferDetails.getTransferAmount());
					fromAccountEntity.setUpdateDateTime(new Date());
					accountEntities.add(fromAccountEntity);

					// Hesabı güncelle
					toAccountEntity.setAccountBalance(toAccountEntity.getAccountBalance() + transferDetails.getTransferAmount());
					toAccountEntity.setUpdateDateTime(new Date());
					accountEntities.add(toAccountEntity);

					accountRepository.saveAll(accountEntities);

					// Hesaptan işlem oluştur
					Transaction fromTransaction = bankingServiceHelper.createTransaction(transferDetails, fromAccountEntity.getAccountNumber(), "DEBIT");
					transactionRepository.save(fromTransaction);

					// Hesap için işlem oluştur
					Transaction toTransaction = bankingServiceHelper.createTransaction(transferDetails, toAccountEntity.getAccountNumber(), "CREDIT");
					transactionRepository.save(toTransaction);
				}

				return ResponseEntity.status(HttpStatus.OK).body("Success: Amount transferred for Customer Number " + customerNumber);
			}

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Number " + customerNumber + " not found.");
		}

	}

	/**
	 * Get all transactions for a specific account
	 *
	 */
	public List<TransactionDetails> findTransactionsByAccountNumber(Long accountNumber) {
		List<TransactionDetails> transactionDetails = new ArrayList<>();
		Optional<Account> accountEntityOpt = accountRepository.findByAccountNumber(accountNumber);
		if(accountEntityOpt.isPresent()) {
			Optional<List<Transaction>> transactionEntitiesOpt = transactionRepository.findByAccountNumber(accountNumber);
			if(transactionEntitiesOpt.isPresent()) {
				transactionEntitiesOpt.get().forEach(transaction -> {
					transactionDetails.add(bankingServiceHelper.convertToTransactionDomain(transaction));
				});
			}
		}

		return transactionDetails;
	}


}

