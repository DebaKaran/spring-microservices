package com.eazybytes.accounts.services.client;

import com.eazybytes.accounts.dto.CardsDto;
import com.eazybytes.accounts.services.client.fallbacks.CardsFallback;
import com.eazybytes.accounts.services.client.fallbacks.CardsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="cards", fallbackFactory = CardsFallbackFactory.class)
public interface CardsFeignClient {

    @GetMapping(value = "/api/fetch")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader("eazybank-correlation-id") String correlationId,
            @RequestParam String mobileNumber);
}
