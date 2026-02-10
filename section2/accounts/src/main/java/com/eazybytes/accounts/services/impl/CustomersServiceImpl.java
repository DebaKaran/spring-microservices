package com.eazybytes.accounts.services.impl;

import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CardsDto;
import com.eazybytes.accounts.dto.CustomerDetailsDto;
import com.eazybytes.accounts.dto.LoansDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exceptions.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.services.ICustomersService;
import com.eazybytes.accounts.services.client.CardsFeignClient;
import com.eazybytes.accounts.services.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansResponse = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);

        LoansDto loansDto = loansResponse.getBody();

        // We check the HEADER, not DTO fields, because:
        // - DTO represents business data
        // - Header represents system health / resilience metadata
        // - Business-valid states (e.g. no loans) must not be treated as failures

        boolean loansServiceDown =
                loansResponse.getHeaders().containsHeader("X-SERVICE-DEGRADED");

        customerDetailsDto.setLoansServiceDown(loansServiceDown);
        customerDetailsDto.setLoansDto(loansDto);

        ResponseEntity<CardsDto> cardsResponse = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        CardsDto cardsDto = cardsResponse.getBody();

        boolean cardsServiceDown =
                cardsResponse.getHeaders().containsHeader("X-SERVICE-DEGRADED");

        customerDetailsDto.setCardsServiceDown(cardsServiceDown);
        customerDetailsDto.setCardsDto(cardsDto);
        return customerDetailsDto;

    }
}