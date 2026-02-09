package com.eazybytes.accounts.services.client.fallbacks;

import com.eazybytes.accounts.dto.LoansDto;
import com.eazybytes.accounts.services.client.LoansFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallback implements LoansFeignClient {
    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        return ResponseEntity.ok(new LoansDto()); // temporary
    }
}
