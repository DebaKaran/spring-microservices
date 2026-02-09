package com.eazybytes.accounts.services.client.fallbacks;

import com.eazybytes.accounts.dto.CardsDto;
import com.eazybytes.accounts.services.client.CardsFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallback implements CardsFeignClient {
    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
        return ResponseEntity.ok(new CardsDto());
    }
}
