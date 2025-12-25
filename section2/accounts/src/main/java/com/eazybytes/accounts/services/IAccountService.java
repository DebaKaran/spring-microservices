package com.eazybytes.accounts.services;

import com.eazybytes.accounts.dto.CustomerDto;

public interface IAccountService {
    void createAccount(final CustomerDto customerDto);
}
