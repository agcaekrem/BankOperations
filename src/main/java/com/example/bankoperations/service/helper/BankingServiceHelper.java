package com.example.bankoperations.service.helper;


import com.example.bankoperations.domain.*;

import com.example.bankoperations.model.Account;
import com.example.bankoperations.model.Contact;
import com.example.bankoperations.model.Customer;
import com.example.bankoperations.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BankingServiceHelper {

	public CustomerDetails convertToCustomerDomain(Customer customer) {
		
		return CustomerDetails.builder()
				.firstName(customer.getFirstName())
				.middleName(customer.getMiddleName())
				.lastName(customer.getLastName())
				.customerNumber(customer.getCustomerNumber())
				.status(customer.getStatus())
				.contactDetails(convertToContactDomain(customer.getContactDetails()))
				.build();
	}
	
	public Customer convertToCustomerEntity(CustomerDetails customerDetails) {
		
		
		return Customer.builder()
				.firstName(customerDetails.getFirstName())
				.middleName(customerDetails.getMiddleName())
				.lastName(customerDetails.getLastName())
				.customerNumber(customerDetails.getCustomerNumber())
				.status(customerDetails.getStatus())
				.contactDetails(convertToContactEntity(customerDetails.getContactDetails()))
				.build();
	}

	public AccountInformation convertToAccountDomain(Account account) {

		return AccountInformation.builder()
				.accountType(account.getAccountType())
				.accountBalance(account.getAccountBalance())
				.accountNumber(account.getAccountNumber())
				.accountStatus(account.getAccountStatus())
				.build();
	}
	
	public Account convertToAccountEntity(AccountInformation accInfo) {
		
		return Account.builder()
				.accountType(accInfo.getAccountType())
				.accountBalance(accInfo.getAccountBalance())
				.accountNumber(accInfo.getAccountNumber())
				.accountStatus(accInfo.getAccountStatus())
				.build();
	}
	

	
	public ContactDetails convertToContactDomain(Contact contact) {
		
		return ContactDetails.builder()
				.emailId(contact.getEmailId())
				.homePhone(contact.getHomePhone())
				.workPhone(contact.getWorkPhone())
				.build();
	}
	
	public Contact convertToContactEntity(ContactDetails contactDetails) {
		
		return Contact.builder()
				.emailId(contactDetails.getEmailId())
				.homePhone(contactDetails.getHomePhone())
				.workPhone(contactDetails.getWorkPhone())
				.build();
	}
	


	public TransactionDetails convertToTransactionDomain(Transaction transaction) {
		
		return TransactionDetails.builder()
									.txAmount(transaction.getTxAmount())
									.txDateTime(transaction.getTxDateTime())
									.txType(transaction.getTxType())
									.accountNumber(transaction.getAccountNumber())
									.build();
	}
	
	public Transaction convertToTransactionEntity(TransactionDetails transactionDetails) {
		
		return Transaction.builder()
							.txAmount(transactionDetails.getTxAmount())
							.txDateTime(transactionDetails.getTxDateTime())
							.txType(transactionDetails.getTxType())
							.accountNumber(transactionDetails.getAccountNumber())
							.build();
	}
	
	public Transaction createTransaction(TransferDetails transferDetails, Long accountNumber, String txType) {
		
		return Transaction.builder()
							.accountNumber(accountNumber)
							.txAmount(transferDetails.getTransferAmount())
							.txType(txType)
							.txDateTime(new Date())
							.build();
	}
}
