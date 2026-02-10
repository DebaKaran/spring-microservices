package com.eazybytes.accounts.services.client;

import com.eazybytes.accounts.dto.LoansDto;
import com.eazybytes.accounts.services.client.fallbacks.LoansFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans", fallbackFactory = LoansFallbackFactory.class)
public interface LoansFeignClient {

    @GetMapping(value = "/api/fetch")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestHeader("eazybank-correlation-id") String correlationId,
            @RequestParam String mobileNumber);

}
